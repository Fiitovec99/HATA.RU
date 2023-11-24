package com.example.hataru.presentation.fragments

import ClusterView
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.hataru.R
import com.example.hataru.databinding.FragmentMapBinding
import com.example.hataru.presentation.MainActivity
import com.example.hataru.presentation.forMap.FlatBottomSheetFragment
import com.example.hataru.presentation.forMap.GeometryProvider
import com.example.hataru.presentation.forMap.flat
import com.example.hataru.presentation.isLocationEnabled
import com.example.hataru.presentation.migration.Roomtypes
import com.example.hataru.presentation.migration.flatsContainer
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
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.map.SizeChangedListener
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.ui_view.ViewProvider
import java.io.Serializable


private const val CLUSTER_RADIUS = 60.0
private const val CLUSTER_MIN_ZOOM = 15

private var flats = flatsContainer.roomTypes.roomtypes

class MapFragment : Fragment() {


    private var savedLatLng: Point? = null // переменная для сохранения координат карты
    private lateinit var binding: FragmentMapBinding
    private lateinit var mapView: MapView
    private lateinit var imageLocation: ImageView
    private val LATITUDE_KEY: String = "LATITUDE"
    private val LONGITUDE_KEY: String = "LONGITUDE"
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                showMyCurrentLocation()
            } else {
                showAlertDialog(
                    activity as AppCompatActivity,
                    "Вы не дали доступ к геолокации,поменяйте его в настроках приложения!"
                )
            }
        }
    private lateinit var collection: MapObjectCollection
    private lateinit var clasterizedCollection: ClusterizedPlacemarkCollection
    private lateinit var locationCollection: ClusterizedPlacemarkCollection

    private val mapWindowSizeChangedListener = SizeChangedListener { _, _, _ ->
        updateFocusRect()
    }
    private val placemarkTapListener = MapObjectTapListener { mapObject, _ -> //TODO
        val flat = mapObject.userData as Roomtypes
        //showToast(flat.cost.toString())
        val bottomSheetFragment = FlatBottomSheetFragment()
        val args = Bundle()
        args.putSerializable("roomtypes", flat as Serializable)
//        args.putInt("id", flat.id!!.toInt())
//        args.putInt("cost", flat.price!!.toDouble().toInt())
        // Передаем параметры в аргументы фрагмента
        bottomSheetFragment.arguments = args
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
        true
    }

    // Sets each cluster appearance using the custom view
    // that shows a cluster's pins
    // слушатель отдаления кластеров
    private val clusterListener = ClusterListener { cluster ->
        val flatsInCluster = cluster.placemarks.mapNotNull { it.userData as? Roomtypes }
        val minValue = flatsInCluster.minByOrNull { it.price?.toDoubleOrNull() ?: Double.MAX_VALUE }?.price?.toDoubleOrNull() ?: 0.0
        val maxValue = flatsInCluster.maxByOrNull { it.price?.toDoubleOrNull() ?: Double.MIN_VALUE }?.price?.toDoubleOrNull() ?: 0.0

        cluster.appearance.setView(
            ViewProvider(
                ClusterView(activity as AppCompatActivity).apply {
                    setData(cluster.size, minValue, maxValue)
                }
            )
        )
        cluster.appearance.zIndex = 100f
        cluster.addClusterTapListener(clusterTapListener)
    }


    // при нажатии на сборище кластеров
    private val clusterTapListener = ClusterTapListener {
        val listPointsOfCluster =
            it.placemarks.map { x: PlacemarkMapObject? -> x?.userData as Roomtypes }
                .map { x: Roomtypes -> Point(x.geoData!!.x!!.toDouble(),x.geoData!!.y!!.toDouble()) }
        if (flatsLocateNearByAnother(listPointsOfCluster, 0.00001)) { // расстояние в меридиане
            showToast("Они близко")
            //просмотр множества квартир
        } else {
            val targetPoint = it.appearance.geometry
            val zoom = 15.0f // Масштаб, на который увеличиваем карту
            mapView.map.move(
                CameraPosition(targetPoint, zoom, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 1f),
                null
            )
        }
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
        if (isLocationEnabled(activity as AppCompatActivity)) {
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
                        showMyLocationIconOnMap(Point(location.latitude, location.longitude))
                    }
                }
                .addOnFailureListener { exception ->
                    showToast("Failed to get location: ")
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
        flatsContainer.clusterizedPoints.forEachIndexed { index, point ->

            val flat = flats[index]
            val markerBitmap = createBitmapWithText(flat.price?.toDouble().toString())
            val priceMarkerImageProvider = ImageProvider.fromBitmap(markerBitmap)

            clasterizedCollection.addPlacemark(
                point,
                priceMarkerImageProvider,
                IconStyle().apply {
                    anchor = PointF(0.5f, 1.0f) // установка "якоря" по координатам
                    scale = 0.65f // размер иконки на карте
                }
            )
                .apply {
                    // Put any data in MapObject
                    userData = flats[index]
                    this.addTapListener(placemarkTapListener)
                }
        }
        clasterizedCollection.clusterPlacemarks(CLUSTER_RADIUS, CLUSTER_MIN_ZOOM)
        //showRostovLocation()//TODO для удобного тестинга
        if ((savedInstanceState != null && !savedInstanceState.isEmpty)) {
            savedInstanceState?.let {
                val latitude = it.getDouble(LATITUDE_KEY, 0.0)
                val longitude = it.getDouble(LONGITUDE_KEY, 0.0)
                mapView.map.move(
                    CameraPosition(Point(latitude, longitude), 14.0f, 0.0f, 0.0f),
                    Animation(Animation.Type.SMOOTH, 0f),
                    null
                )

            }
        } else {
            if (isLocationEnabled(activity as AppCompatActivity)) {
                showMyCurrentLocation()
            } else {
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
        if (isLocationEnabled(activity as AppCompatActivity)) {
            showMyCurrentLocation()
            showMyCurrentLocationWithIconOnMap() //TODO косяк, нужно исправлять
        } else {
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

    private fun showMyCurrentLocation() {
        val locationManager = MapKitFactory.getInstance().createLocationManager()
        locationManager.requestSingleUpdate(object : LocationListener {
            override fun onLocationUpdated(location: Location) {
                val currentPosition = Point(location.position.latitude, location.position.longitude)
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

    private fun showMyCurrentLocationWithIconOnMap() {
        val locationManager = MapKitFactory.getInstance().createLocationManager()
        locationManager.requestSingleUpdate(object : LocationListener {
            override fun onLocationUpdated(location: Location) {
                val currentPosition =
                    Point(location.position.latitude, location.position.longitude)
                clasterizedCollection
                showMyLocationIconOnMap(
                    Point(
                        location.position.latitude,
                        location.position.longitude
                    )
                )
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
        icon.setIconStyle(
            IconStyle().setAnchor(
                PointF(0.5f, 0.5f)
            )
        )
    }

    private fun initImageLocation() {
        imageLocation = binding.imageLocation
    }

    private fun initMap() {
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

    private fun flatsLocateNearByAnother(
        listPointsOfCluster: List<Point>,
        thresholdDistance: Double
    ): Boolean {
        for (i in 0 until listPointsOfCluster.size - 1) {
            val point1 = listPointsOfCluster[i]
            for (j in i + 1 until listPointsOfCluster.size) {
                val point2 = listPointsOfCluster[j]
                val distance = calculateDistance(point1, point2)
                if (distance > thresholdDistance) {
                    return false
                }
            }
        }
        return true
    }

    private fun calculateDistance(point1: Point, point2: Point): Double {
        return Math.sqrt(
            Math.pow(point1.latitude - point2.latitude, 2.0) +
                    Math.pow(point1.longitude - point2.longitude, 2.0)
        )
    }

    private fun initializeMap() {
        MapKitFactory.initialize(requireActivity() as MainActivity)
    }

    private fun createBitmapWithText(text: String): Bitmap {
        val textSize = resources.getDimension(R.dimen.text_size)
        val textColor = Color.WHITE
        val padding = 70

        val ovalWidth = 300 // Ширина овала
        val ovalHeight = 200 // Высота овала
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = textColor

            textAlign = Paint.Align.CENTER
        }
        paint.textSize = textSize
        val textBounds = Rect()
        paint.getTextBounds(text, 0, text.length, textBounds)

        val textWidth = textBounds.width()
        val textHeight = textBounds.height()

        val width = ovalWidth + 2 * padding
        val height = ovalHeight + 2 * padding

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Рисуем овал
        val ovalRect = RectF(
            padding.toFloat(),
            padding.toFloat(),
            (width - padding).toFloat(),
            (height - padding).toFloat()
        )
        paint.color = ContextCompat.getColor(requireContext(), R.color.flatWidgetOnMap)
        canvas.drawRoundRect(ovalRect, ovalWidth.toFloat(), ovalHeight.toFloat(), paint)

        // Рисуем текст внутри овала
        paint.color = textColor
        val textX = width / 2.toFloat() // Позиция X текста по центру овала
        val textY =
            height / 2.toFloat() + textHeight / 2.toFloat() // Позиция Y текста по центру овала
        canvas.drawText(text, textX, textY, paint)
        return bitmap
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