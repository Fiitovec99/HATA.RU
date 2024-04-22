package com.example.hataru.presentation.fragments

import ApartmentsViewPagerFragment
import ApartmentsViewPagerFragment.Companion.KEY_GET_FLAT_INTO_ADAPTER
import android.Manifest
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hataru.R
import com.example.hataru.databinding.FragmentMapBinding
import com.example.hataru.domain.entity.Roomtype
import com.example.hataru.domain.entity.RoomtypeWithPhotos
import com.example.hataru.isLocationEnabled
import com.example.hataru.presentation.ClusterView
import com.example.hataru.presentation.adapter.RoomtypeAdapter
import com.example.hataru.presentation.fragments.FlatBottomSheetFragment.Companion.KEY_GET_FLAT
import com.example.hataru.presentation.viewModels.MapViewModel
import com.example.hataru.showAlertDialog
import com.example.hataru.showToast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.slider.RangeSlider
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.ScreenPoint
import com.yandex.mapkit.ScreenRect
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationStatus
import com.yandex.mapkit.logo.Alignment
import com.yandex.mapkit.logo.HorizontalAlignment
import com.yandex.mapkit.logo.VerticalAlignment
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.Cluster
import com.yandex.mapkit.map.ClusterListener
import com.yandex.mapkit.map.ClusterTapListener
import com.yandex.mapkit.map.ClusterizedPlacemarkCollection
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.map.SizeChangedListener
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.ui_view.ViewProvider
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.Serializable


private const val CLUSTER_RADIUS = 60.0
private const val CLUSTER_MIN_ZOOM = 18

val startPosition = CameraPosition(Point(47.222078, 39.720358), 14.0f, 0.0f, 0.0f)

class MapFragment : Fragment(), CameraListener, ViewTreeObserver.OnPreDrawListener {

    private lateinit var adapter: RoomtypeAdapter
    private val viewModel by viewModel<MapViewModel>()
    private lateinit var binding: FragmentMapBinding
    private lateinit var mapView: MapView
    private lateinit var imageLocation: ImageView
    private var flats: List<Roomtype>? = null

    private var roomtypeWithPhotosList: List<RoomtypeWithPhotos> = emptyList()

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

    private val mapWindowSizeChangedListener = SizeChangedListener { _, _, _ ->
        updateFocusRect()
    }
    private val placemarkTapListener = MapObjectTapListener { mapObject, _ ->
        showFlatDetailsBySheetFragment(mapObject.userData as Roomtype)
        true
    }

    private val clusterListener = ClusterListener { cluster ->
        handleClusterTap(cluster)
    }

    // при нажатии на сборище кластеров
    private val clusterTapListener = ClusterTapListener {
        handleMultiClusterTap(it)
        true
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(inflater, container, false)

        MapKitFactory.initialize(requireContext())
        initImageLocation()



        mapView = binding.mapview
        mapView.map.logo.setAlignment(Alignment(HorizontalAlignment.RIGHT, VerticalAlignment.TOP))

        setBottomSheetWithSetting()


        viewModel.visibleFlats.observe(viewLifecycleOwner) { visibleFlats ->
            binding.countFlatsOnMap.text =
                "Обнаружено " + getRigthStringForCountFlatsOnMap(visibleFlats?.size ?: 3)
        }


//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
//        if (isLocationEnabled(activity as AppCompatActivity)) {
//            if (ActivityCompat.checkSelfPermission(
//                    activity as AppCompatActivity,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                    activity as AppCompatActivity,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//
//            }
//            fusedLocationClient.lastLocation
//                .addOnSuccessListener { location ->
//                    if (location != null) { //TODO
//                        showMyLocationIconOnMap(Point(location.latitude, location.longitude))
//                    }
//                }
//                .addOnFailureListener { exception ->
//                    showToast("Failed to get location: ")
//                    // Ошибка получения местоположения
//                }
//        }
        return binding.root
    }

    private fun setBottomSheetWithSetting() {
        val bottomSheet = binding.persistentBottomSheet
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.isHideable = false
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.isDraggable = true

        bottomSheet.setOnClickListener {
            val str = binding.countFlatsOnMap.text.toString()

            if (Regex(".*\\b1\\b.*").matches(str)) {
                val flat = viewModel.visibleFlats.value!![0]
                showFlatDetailsBySheetFragment(flat)
                return@setOnClickListener
            }

            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED &&
                !Regex(".*\\b0\\b.*").matches(str)
            ) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }

        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                val isBottomSheetExpanded = newState == BottomSheetBehavior.STATE_EXPANDED
                val isBottomSheetHidden = newState == BottomSheetBehavior.STATE_HIDDEN

                // Выполняем нужные действия в зависимости от состояния BottomSheet
                if (isBottomSheetExpanded) {


//                    viewModel.visibleFlats.observe(viewLifecycleOwner) { flats ->
//                        val roomtypeWithPhotosList = flats?.map { RoomtypeWithPhotos(it, emptyList()) }
//                        adapter.submitList(roomtypeWithPhotosList)
//                    }

                    viewModel.combinedData.observe(viewLifecycleOwner) { (roomtypes, roomxList) ->
                        roomtypes?.let { roomtypes ->
                            roomxList?.let { roomxList ->
                                roomtypeWithPhotosList = roomtypes.map { roomtype ->
                                    val matchingPhoto = roomxList.firstOrNull { roomx ->
                                        roomx.name == roomtype.name
                                    }?.photos ?: emptyList()
                                    RoomtypeWithPhotos(roomtype, matchingPhoto)
                                }
                                adapter.submitList(roomtypeWithPhotosList)

                            }
                        }
                    }


                    // BottomSheet был выдвинут
                    // Выполняем нужные действия
                } else if (isBottomSheetHidden) {
                    // BottomSheet был скрыт
                    // Выполняем нужные действия
                } else {

                }
                updateMapViewInteraction(newState)
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // Handle slide offset changes if needed
            }
        })
    }

//    val recyclerView = binding.recyclerViewBottomSheet
//
//    val adapter = FlatListOnMap(emptyList())
//    viewModel.visibleFlats.observe(viewLifecycleOwner) { visibleFlats ->
//        if (visibleFlats != null) {
//            adapter.updateFlats(visibleFlats)
//        }
//        binding.countFlatsOnMap.text = "Обнаружено " + getRigthStringForCountFlatsOnMap(visibleFlats?.size ?: 3)
//        recyclerView.adapter = adapter
//    }


    private fun setupApartmentClickListener() {

        adapter.onApartmentClickListener = {

            val args = Bundle()
            args.putParcelable(
                FlatFragment.KEY_GET_FLAT_INTO_FLATFRAGMENT,
                it.roomtype as Parcelable
            )

            findNavController().navigate(R.id.flatFragment, args)

        }
        adapter.onLikeButtonClickListener = { flat ->
            viewModel.changeLikedStage(flat)
            showToast("Квартира добавлена в избранные!")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //TODO fusedLocationClient.lastLocation
        // Освобождение других ресурсов, связанных с местоположением, если они есть
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap()

        setFirstFlats()

        adapter = RoomtypeAdapter(roomtypeWithPhotosList, requireContext())
        setupApartmentClickListener()
        binding.recyclerViewBottomSheet.adapter = adapter
        mapView.map.addCameraListener(this)

        setFiltersClickListener()

        collection = mapView.map.mapObjects.addCollection()
        clasterizedCollection = collection.addClusterizedPlacemarkCollection(clusterListener)
        mapView.mapWindow.addSizeChangedListener(mapWindowSizeChangedListener)
        updateFocusRect()

        viewModel.flats.observe(viewLifecycleOwner) {

            flats = it
            showFlatsOnMap(it)
        }


        //showRostovLocation()//TODO для удобного тестинга
        if (viewModel.latitude != 0.0 && viewModel.longitude != 0.0) {
            val currentPosition = Point(viewModel.latitude, viewModel.longitude)
            mapView.map.move(
                CameraPosition(currentPosition, viewModel.zoom, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 0f),
                null
            )
        } else {
            if (isLocationEnabled(activity as AppCompatActivity)) {
                showMyCurrentLocation()
            } else {
                showRostovLocation()
            }
        }

    }

    fun getFlatsWithFilter(price_min: Double, price_max: Double): List<Roomtype> {
        val flats = viewModel.flats.value
        if (flats != null)
            return flats.filter { flat: Roomtype -> flat.price.toDouble() >= price_min && flat.price.toDouble() <= price_max }
        else
            return listOf()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setFiltersClickListener() {
        binding.apply {

            deleteFilter.setOnClickListener {
                binding.persistentBottomSheet.visibility = View.VISIBLE
                clasterizedCollection.clear()
                showFlatsOnMap(viewModel.flats.value!!.toList())
                currentCostTextView.visibility = View.GONE
            }

            buttonFilterFlats.setOnClickListener {
                showPreferencesPopup(it)
            }


        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showPreferencesPopup(anchorView: View) {

        val inflater = LayoutInflater.from(requireContext())
        val popupView = inflater.inflate(R.layout.layout_filter_preferences, null)

        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        popupWindow.isFocusable = true
        popupWindow.isTouchable = true
        popupWindow.isOutsideTouchable = true
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        val btnApplyPreferences = popupView.findViewById<Button>(R.id.btnApplyPreferences)
        val rangeSlider = popupView.findViewById<RangeSlider>(R.id.rangeSlider)


        // Установка пределов для RangeSlider
        rangeSlider.valueFrom = 1000f
        rangeSlider.valueTo = 5000f
        rangeSlider.values = listOf(1000f, 5000f) // Устанавливаем начальные значения
        rangeSlider.stepSize = 1f

        popupView.findViewById<TextView>(R.id.tvSelectedPrice).text = "От 1000 до 5000"
        // Обработчик изменения значения RangeSlider
        rangeSlider.addOnChangeListener { slider, _, _ ->
            val startPrice = slider.values[0].toInt()
            val endPrice = slider.values[1].toInt()
            // Обновляем текстовое представление с выбранным диапазоном цен
            val tvSelectedPrice = popupView.findViewById<TextView>(R.id.tvSelectedPrice)
            tvSelectedPrice.text = "От $startPrice до $endPrice"
        }

        btnApplyPreferences.setOnClickListener {
            val currentsFlatWithDiapozon = getFlatsWithFilter(
                rangeSlider.values[0].toDouble(),
                rangeSlider.values[1].toDouble()
            )
            clasterizedCollection.clear()
            showFlatsOnMap(currentsFlatWithDiapozon)
            binding.persistentBottomSheet.visibility = View.GONE

            binding.currentCostTextView.visibility = View.VISIBLE
            binding.currentCostTextView.text =
                "текущий диапозон цен: \nот" + rangeSlider.values[0].toInt()
                    .toString() + " до " + rangeSlider.values[1].toInt().toString()

        }

        popupWindow.showAtLocation(anchorView, Gravity.TOP, 0, 0)
    }


    private fun showFlatsOnMap(lst: List<Roomtype>) {
        lst.map { x: Roomtype ->
            Point(
                x.geo_data!!.x!!.toDouble(),
                x!!.geo_data!!.y!!.toDouble()
            )
        }?.forEachIndexed { index, point ->

            val flat = lst!![index]
            val markerBitmap = createBitmapWithText(flat.price!!.toDouble().toInt().toString())
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
                    userData = flat // Put any data in MapObject
                    this.addTapListener(placemarkTapListener)
                }
        }
        clasterizedCollection.clusterPlacemarks(CLUSTER_RADIUS, CLUSTER_MIN_ZOOM)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val cameraPosition = mapView.map.cameraPosition
        viewModel.latitude = cameraPosition.target.latitude
        viewModel.longitude = cameraPosition.target.longitude
        viewModel.zoom = cameraPosition.zoom

        mapView.map.removeCameraListener(this)

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

    private fun handleMultiClusterTap(it: Cluster) {
        val listPointsOfCluster =
            it.placemarks.map { x: PlacemarkMapObject? -> x?.userData as Roomtype }
                .map { x: Roomtype ->
                    Point(
                        x.geo_data!!.x!!.toDouble(),
                        x.geo_data!!.y!!.toDouble()
                    )
                }

        if (flatsLocateNearByAnother(listPointsOfCluster, 0.00001)) { // расстояние в меридиане
            val flats = it.placemarks?.mapNotNull { it.userData as? Roomtype }
            val args = Bundle()
            args.putSerializable(KEY_GET_FLAT_INTO_ADAPTER, flats as? Serializable)
            val viewPagerFragment = ApartmentsViewPagerFragment()
            viewPagerFragment.arguments = args
            findNavController().navigate(R.id.apartmentsViewPagerFragment, args)

        } else {
            val targetPoint = it.appearance.geometry
            val currentZoom = mapView.map.cameraPosition.zoom
            val zoom = if (currentZoom >= 15.0f) currentZoom + 2.0f else 15.0f
            mapView.map.move(
                CameraPosition(targetPoint, zoom, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 1f),
                null
            )
        }
    }

    private fun handleClusterTap(cluster: Cluster) {
        val flatsInCluster = cluster.placemarks.mapNotNull { it.userData as? Roomtype }
        val minValue = (flatsInCluster.minByOrNull { it.price }?.price)!!.toDouble()
        val maxValue = (flatsInCluster.maxByOrNull { it.price }?.price)!!.toDouble()

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

    private fun showFlatDetailsBySheetFragment(flat: Roomtype) {
        val bottomSheetFragment = FlatBottomSheetFragment()
        val args = Bundle()
        args.putParcelable(KEY_GET_FLAT, flat as Parcelable)
        bottomSheetFragment.arguments = args
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
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

    override fun onCameraPositionChanged(
        p0: Map,
        p1: CameraPosition,
        p2: CameraUpdateReason,
        p3: Boolean,
    ) {
        val visibleRegion = mapView.map.visibleRegion
        val visibleFlats = flats?.filter { flat ->
            flat.geo_data?.let { geoData ->
                val x = geoData.x?.toDoubleOrNull()
                val y = geoData.y?.toDoubleOrNull()

                if (x != null && y != null) {
                    val flatPoint = Point(x, y)
                    isPointInVisibleRegion(
                        flatPoint,
                        visibleRegion.topLeft,
                        visibleRegion.bottomRight
                    )
                } else {
                    false
                }
            } ?: false
        }

        viewModel.updateVisibleFlats(visibleFlats)
    }

    private fun setFirstFlats() {
        val visibleRegion = mapView.map.visibleRegion
        val visibleFlats = flats?.filter { flat ->
            flat.geo_data?.let { geoData ->
                val x = geoData.x?.toDoubleOrNull()
                val y = geoData.y?.toDoubleOrNull()

                if (x != null && y != null) {
                    val flatPoint = Point(x, y)
                    isPointInVisibleRegion(
                        flatPoint,
                        visibleRegion.topLeft,
                        visibleRegion.bottomRight
                    )
                } else {
                    false
                }
            } ?: false
        }

        viewModel.updateVisibleFlats(visibleFlats)
    }


    private fun updateMapViewInteraction(bottomSheetState: Int) {
        if (bottomSheetState == BottomSheetBehavior.STATE_EXPANDED ||
            bottomSheetState == BottomSheetBehavior.STATE_HALF_EXPANDED
        ) {
            // BottomSheet развернут, блокируем карту
            mapView.map.isScrollGesturesEnabled = false
            mapView.map.isZoomGesturesEnabled = false
            mapView.map.isTiltGesturesEnabled = false
            mapView.map.isRotateGesturesEnabled = false
            mapView.map.isFastTapEnabled = false
            mapView.isClickable = false
            mapView.isEnabled = false
            mapView.map
        } else {
            // BottomSheet свёрнут, разблокируем карту
            mapView.map.isScrollGesturesEnabled = true
            mapView.map.isZoomGesturesEnabled = true
            mapView.map.isTiltGesturesEnabled = true
            mapView.map.isRotateGesturesEnabled = true
            mapView.map.isFastTapEnabled = true
            mapView.isClickable = true
            mapView.isEnabled = true
        }
    }

    private fun isPointInVisibleRegion(point: Point, topLeft: Point, bottomRight: Point): Boolean {
        return (point.latitude in bottomRight.latitude..topLeft.latitude) &&
                (point.longitude in topLeft.longitude..bottomRight.longitude)
    }

    fun getRigthStringForCountFlatsOnMap(count: Int): String {
        val lastDigit = count % 10
        val lastTwoDigits = count % 100

        return when {
            lastDigit == 1 && lastTwoDigits != 11 -> "$count квартира"
            (lastDigit in 2..4 && !(lastTwoDigits in 12..14)) -> "$count квартиры"
            else -> "$count квартир"
        }
    }

    override fun onPreDraw(): Boolean {
        mapView.viewTreeObserver.removeOnPreDrawListener(this)
        // Здесь вы можете выполнить дополнительные действия после отрисовки карты
        return true

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
            startPosition,
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

}