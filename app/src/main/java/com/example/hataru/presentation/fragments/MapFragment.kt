package com.example.hataru.presentation.fragments

import ClusterView
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.PointF
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.hataru.R
import com.example.hataru.databinding.FragmentMapBinding
import com.example.hataru.presentation.MainActivity
import com.example.hataru.presentation.forMap.FlatBottomSheetFragment
import com.example.hataru.presentation.forMap.GeometryProvider
import com.example.hataru.presentation.forMap.flat
import com.example.hataru.presentation.isLocationEnabled
import com.example.hataru.presentation.showAlertDialog
import com.example.hataru.presentation.showToast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.ScreenPoint
import com.yandex.mapkit.ScreenRect
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationStatus
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.ClusterListener
import com.yandex.mapkit.map.ClusterTapListener
import com.yandex.mapkit.map.ClusterizedPlacemarkCollection
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.SizeChangedListener
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.ui_view.ViewProvider


private const val CLUSTER_RADIUS = 60.0
private const val CLUSTER_MIN_ZOOM = 15
class MapFragment : Fragment() {




    private var savedLatLng: Point? = null // переменная для сохранения координат карты


    private lateinit var binding: FragmentMapBinding
    private lateinit var mapView: MapView
    private lateinit var imageLocation: ImageView
    private val LATITUDE_KEY : String = "LATITUDE"
    private val LONGITUDE_KEY : String = "LONGITUDE"

    private lateinit var fusedLocationClient: FusedLocationProviderClient

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

        showToast(mapObject.userData.toString())
//        val bottomSheetFragment = FlatBottomSheetFragment()
//        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)

         true
    }


    // слушатель отдаления кластеров
    private val clusterListener = ClusterListener { cluster ->
        // Sets each cluster appearance using the custom view
        // that shows a cluster's pins
        cluster.appearance.setView(
            ViewProvider(
                ClusterView(activity as AppCompatActivity).apply {
                    setData(cluster.size)
                }
            )
        )
        cluster.appearance.zIndex = 100f
        cluster.addClusterTapListener(clusterTapListener)
    }

    // при нажатии на сборище кластеров
    // вдруг пригодится
    private val clusterTapListener = ClusterTapListener {
        showToast("Clicked on cluster with ${it.size} items")
        true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Сохраняем текущие координаты карты
        mapView?.let {
            savedLatLng = mapView.mapWindow.map.cameraPosition.target
            outState.putDouble(LATITUDE_KEY, savedLatLng?.latitude ?: 0.0)
            outState.putDouble(LONGITUDE_KEY, savedLatLng?.longitude ?: 0.0)


        }
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        initializeMap()
        initImageLocation()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        if(isLocationEnabled(activity as AppCompatActivity)){
            if (ActivityCompat.checkSelfPermission(
                    activity as AppCompatActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    activity as AppCompatActivity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

            }
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) { //TODO
                        showMyLocationIconOnMap(Point(location.latitude,location.longitude))
                    }
                }
                .addOnFailureListener { exception ->
                    // Ошибка получения местоположения
                }
        }


        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
       //TODO fusedLocationClient.lastLocation
        // Освобождение других ресурсов, связанных с местоположением, если они есть
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap()

        collection = mapView.map.mapObjects.addCollection()

        clasterizedCollection = collection.addClusterizedPlacemarkCollection(clusterListener)
        mapView.mapWindow.addSizeChangedListener(mapWindowSizeChangedListener)
        updateFocusRect()


        GeometryProvider.clusterizedPoints.forEachIndexed { index, point ->

            val imageProvider = ImageProvider.fromResource(context, R.drawable.pin_green)

            clasterizedCollection.addPlacemark(
                point,
                imageProvider,
                IconStyle().apply {
                    anchor = PointF(0.5f, 1.0f)
                    scale = 0.6f
                }
            )
                .apply {
                    // Put any data in MapObject
                    //PlacemarkUserData("Data_$index", type)

                    userData = flat(index,point)
                    this.addTapListener(placemarkTapListener)
                }
        }

        clasterizedCollection.clusterPlacemarks(CLUSTER_RADIUS, CLUSTER_MIN_ZOOM)



        if((savedInstanceState != null && !savedInstanceState.isEmpty)) {
            savedInstanceState?.let {



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
            showMyCurrentLocationWithIconOnMap() //TODO косяк, нужно исправлять
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
                
                // Перемещение карты в текущее местоположение

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

    private fun showMyCurrentLocationWithIconOnMap(){
        val locationManager = MapKitFactory.getInstance().createLocationManager()
        locationManager.requestSingleUpdate(object : LocationListener {
            override fun onLocationUpdated(location: Location) {
                val currentPosition =
                    Point(location.position.latitude, location.position.longitude)

                // Перемещение карты в текущее местоположение
                showMyLocationIconOnMap(Point(location.position.latitude, location.position.longitude))
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