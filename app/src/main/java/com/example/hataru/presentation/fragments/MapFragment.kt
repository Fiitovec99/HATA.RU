package com.example.hataru.presentation.fragments

import ClusterView
import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.graphics.PointF
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.hataru.R

import com.example.hataru.databinding.FragmentMapBinding
import com.example.hataru.presentation.MainActivity
import com.example.hataru.presentation.forMap.GeometryProvider

import com.example.hataru.presentation.forMap.PlacemarkType
import com.example.hataru.presentation.forMap.PlacemarkUserData
import com.example.hataru.presentation.isLocationEnabled
import com.example.hataru.presentation.showAlertDialog
import com.example.hataru.presentation.showToast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.ScreenPoint
import com.yandex.mapkit.ScreenRect
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationStatus
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CircleMapObject
import com.yandex.mapkit.map.ClusterListener
import com.yandex.mapkit.map.ClusterTapListener
import com.yandex.mapkit.map.ClusterizedPlacemarkCollection
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectDragListener
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.MapObjectVisitor
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.map.PolygonMapObject
import com.yandex.mapkit.map.PolylineMapObject
import com.yandex.mapkit.map.SizeChangedListener
import com.yandex.mapkit.map.TextStyle
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.ui_view.ViewProvider
import com.yandex.mapkit.map.ClusterizedPlacemarkCollection as ClusterizedPlacemarkCollection1


private const val CLUSTER_RADIUS = 60.0
private const val CLUSTER_MIN_ZOOM = 15
class MapFragment : Fragment() {


    private var savedLatLng: Point? = null // переменная для сохранения координат карты


    private lateinit var binding: FragmentMapBinding
    private lateinit var mapView: MapView
    private lateinit var imageLocation: ImageView
    private val LATITUDE_KEY : String = "LATITUDE"
    private val LONGITUDE_KEY : String = "LONGITUDE"
    private var currentLatutude : Double ? = null
    private var currentLongitude : Double ?=null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                showMyCurrentLocation()
            } else {
                showAlertDialog(activity as AppCompatActivity, "Вы не дали доступ к геолокации,поменяйте его в настроках приложения!")
            }
        }

    private lateinit var collection: MapObjectCollection
    private lateinit var clasterizedCollection: ClusterizedPlacemarkCollection


    private val mapWindowSizeChangedListener = SizeChangedListener { _, _, _ ->
        updateFocusRect()
    }
    private val placemarkTapListener = MapObjectTapListener { mapObject, _ -> //TODO

        showAlertDialog(activity as AppCompatActivity,"Tapped the placemark: ${mapObject.userData}")
         true
    }
    private val pinDragListener = object : MapObjectDragListener {
        override fun onMapObjectDragStart(p0: MapObject) {
            showToast("Start drag event")
        }

        override fun onMapObjectDrag(p0: MapObject, p1: Point) = Unit

        override fun onMapObjectDragEnd(p0: MapObject) {
            showToast("End drag event")
            // Updates clusters position
            clasterizedCollection.clusterPlacemarks(CLUSTER_RADIUS, CLUSTER_MIN_ZOOM)
        }
    }
    private val clusterListener = ClusterListener { cluster ->
        val placemarkTypes = cluster.placemarks.map {
            (it.userData as PlacemarkUserData).type
        }
        // Sets each cluster appearance using the custom view
        // that shows a cluster's pins
        cluster.appearance.setView(
            ViewProvider(
                ClusterView(activity as AppCompatActivity).apply {
                    setData(placemarkTypes)
                }
            )
        )
        cluster.appearance.zIndex = 100f

        cluster.addClusterTapListener(clusterTapListener)
    }

    private val clusterTapListener = ClusterTapListener {
        showToast("Clicked on cluster with ${it.size} items")
        true
    }

    private val singlePlacemarkTapListener = MapObjectTapListener { _, _ ->
        showToast("Clicked the placemark with composite icon")
        true
    }
    private lateinit var locationManager: LocationManager


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Сохраняем текущие координаты карты
        mapView?.let {
            savedLatLng = mapView.mapWindow.map.cameraPosition.target
            outState.putDouble(LATITUDE_KEY, savedLatLng?.latitude ?: 0.0)
            outState.putDouble(LONGITUDE_KEY, savedLatLng?.longitude ?: 0.0)

            outState.putDouble("1", currentLatutude ?: 0.0)
            outState.putDouble("2", currentLongitude ?: 0.0)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        initializeMap()
        initImageLocation()
        //locationManager =


        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap()

        collection = mapView.map.mapObjects.addCollection()

        clasterizedCollection = collection.addClusterizedPlacemarkCollection(clusterListener)
        mapView.mapWindow.addSizeChangedListener(mapWindowSizeChangedListener)
        updateFocusRect()


        // Add pins to the clusterized collection

        val placemarkTypeToImageProvider = mapOf(
            PlacemarkType.GREEN to ImageProvider.fromResource(activity as AppCompatActivity, R.drawable.pin_green),
            PlacemarkType.YELLOW to ImageProvider.fromResource(activity as AppCompatActivity, R.drawable.pin_yellow),
            PlacemarkType.RED to ImageProvider.fromResource(activity as AppCompatActivity, R.drawable.pin_red),
        )


        GeometryProvider.clusterizedPoints.forEachIndexed { index, point ->
            val type = PlacemarkType.values().random()
            val imageProvider = placemarkTypeToImageProvider[type]!!
            clasterizedCollection.addPlacemark(
                point,
                imageProvider,
                IconStyle().apply {
                    anchor = PointF(0.5f, 1.0f)
                    scale = 0.6f
                }
            )
                .apply {
                    // If we want to make placemarks draggable, we should call
                    // clasterizedCollection.clusterPlacemarks on onMapObjectDragEnd
                    isDraggable = true
                    setDragListener(pinDragListener)
                    // Put any data in MapObject
                    userData = PlacemarkUserData("Data_$index", type)
                    this.addTapListener(placemarkTapListener)
                }
        }

        clasterizedCollection.clusterPlacemarks(CLUSTER_RADIUS, CLUSTER_MIN_ZOOM)

        // Composite placemark with text
        val placemark = collection.addPlacemark(GeometryProvider.compositeIconPoint).apply {
            addTapListener(singlePlacemarkTapListener)
            // Set text near the placemark with the custom TextStyle
            setText(
                "Special place",
                TextStyle().apply {
                    size = 10f
                    placement = TextStyle.Placement.RIGHT
                    offset = 5f
                },
            )
        }

        placemark.useCompositeIcon().apply {
            // Combine several icons in the single composite icon
            setIcon(
                "pin",
                ImageProvider.fromResource(activity as AppCompatActivity, R.drawable.pin_green),
                IconStyle().apply {
                    anchor = PointF(0.5f, 1.0f)
                    scale = 0.9f
                }
            )
            setIcon(
                "point",
                ImageProvider.fromResource(activity as AppCompatActivity, R.drawable.ic_circle),
                IconStyle().apply {
                    anchor = PointF(0.5f, 0.5f)
                    flat = true
                    scale = 0.05f
                }
            )
        }

        if((savedInstanceState != null && !savedInstanceState.isEmpty)) {
            savedInstanceState?.let {
                showMyLocationIconOnMap(Point(it.getDouble("1"),it.getDouble("2")))
                val latitude = it.getDouble(LATITUDE_KEY, 0.0)
                val longitude = it.getDouble(LONGITUDE_KEY, 0.0)
                mapView.map.move(
                    CameraPosition(Point(latitude, longitude), 14.0f, 0.0f, 0.0f),
                    Animation(Animation.Type.SMOOTH, 0f),
                    null
                )

            }}
        else{
            if(isLocationEnabled(activity as AppCompatActivity)){
                showMyCurrentLocation()
            }else{
                showRostovLocation()
            }
        }

    }



    private fun locationClickListener() {
        imageLocation.setOnClickListener {
            checkLocationAndMoveMap()
        }
    }

    private fun checkLocationAndMoveMap() {
        if(isLocationEnabled(activity as AppCompatActivity)){
            showMyCurrentLocation()
        }else{
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }




    private fun showRostovLocation() {
        mapView.map.move(
            GeometryProvider.startPosition,
            Animation(Animation.Type.SMOOTH, 0f),
            null
        )
    }


    private fun showMyCurrentLocation(){
        val locationManager = MapKitFactory.getInstance().createLocationManager()
        locationManager.requestSingleUpdate(object : LocationListener {
            override fun onLocationUpdated(location: Location) {
                val currentPosition =
                    Point(location.position.latitude, location.position.longitude)
                currentLatutude = currentPosition.latitude
                currentLongitude = currentPosition.longitude

                // Перемещение карты в текущее местоположение
                showMyLocationIconOnMap(currentPosition)
                mapView.map.move(
                    CameraPosition(currentPosition, 14.0f, 0.0f, 0.0f),
                    Animation(Animation.Type.SMOOTH, 2f),
                    null
                )
            }
            override fun onLocationStatusUpdated(locationStatus: LocationStatus) {

            }
        })
    }

    private fun showMyLocationIconOnMap(position: Point) {
        //mapView.map.mapObjects.clear()

        val icon = mapView.map.mapObjects.addPlacemark(position)
        //icon.setIcon(ImageProvider.fromResource(activity as AppCompatActivity, R.drawable.location_icon48dp))
        icon.setIconStyle(IconStyle().setScale(2.0f))
        icon.setIconStyle(IconStyle().setAnchor(PointF(0.5f, 0.5f))) // Устанавливаем якорь в центр иконки
    }


    private fun initImageLocation() {
        imageLocation = binding.imageLocation
    }


    fun initMap() {
        mapView = binding.mapview
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart();
        locationClickListener()
    }

    private fun initializeMap() {
        MapKitFactory.initialize(requireActivity() as MainActivity)
    }


    private fun updateFocusRect() {
        val horizontalMargin = 40f
        val verticalMargin = 60f
        mapView.mapWindow.focusRect = ScreenRect(
            ScreenPoint(horizontalMargin, verticalMargin),
            ScreenPoint(
                mapView.mapWindow.width() - horizontalMargin,
                mapView.mapWindow.height() - verticalMargin
            )
        )
    }



}