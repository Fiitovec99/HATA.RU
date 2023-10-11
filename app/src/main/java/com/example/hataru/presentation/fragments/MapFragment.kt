package com.example.hataru.presentation.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.PointF
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.hataru.databinding.FragmentMapBinding
import com.example.hataru.presentation.MainActivity
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationStatus
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.mapview.MapView

class MapFragment : Fragment() {

//    companion object {
//        fun newInstance() = MapFragment()
//    }
//
//    private lateinit var viewModel: MapViewModel
    private var savedLatLng: Point? = null // переменная для сохранения координат карты

    private lateinit var binding: FragmentMapBinding
    private lateinit var mapView: MapView
    private lateinit var imageLocation: ImageView
    private val LATITUDE_KEY : String = "LATITUDE"
    private val LONGITUDE_KEY : String = "LONGITUDE"
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                showMyCurrentLocation()
            } else {
                val longText = "Вы не дали доступ к геолокации,поменяйте его в настроках приложения!"
                val alertDialog = AlertDialog.Builder(activity as AppCompatActivity)
                    .setMessage(longText)
                    .setPositiveButton("ОК") { dialog, _ -> dialog.dismiss() }
                    .create()
                alertDialog.show()
            }
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
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(savedInstanceState != null && !savedInstanceState.isEmpty) {
            savedInstanceState?.let {
                val latitude = it.getDouble(LATITUDE_KEY, 0.0)
                val longitude = it.getDouble(LONGITUDE_KEY, 0.0)
                mapView.map.move(
                    CameraPosition(Point(latitude, longitude), 14.0f, 0.0f, 0.0f),
                    Animation(Animation.Type.SMOOTH, 0f),
                    null
                )
            }}else{
            if(isLocationEnabled(activity as AppCompatActivity)){
                showMyCurrentLocation()
            }else{
                showRostovLocation()
            }
        }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        initializeMap()
        initImageLocation()

        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap()



        showFlatsIcons();
    }

    private fun showFlatsIcons(){
//        var arr = listOf(  Point(47.229271, 39.713218), Point(47.225141, 39.730034),
//            Point(47.225141, 39.730034) )
//
//        for(i in 0..2){
//            val icon = mapView.map.mapObjects.addPlacemark(arr[i])
//            icon.setIcon(ImageProvider.fromResource(activity as AppCompatActivity, R.drawable.location_icon48dp))
//            icon.setIconStyle(IconStyle().setScale(2.0f))
//            icon.setIconStyle(IconStyle().setAnchor(PointF(0.5f, 0.5f))) // Устанавливаем якорь в центр иконки
//        }
//
//
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





    private fun isLocationEnabled(context: Context): Boolean {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        val granted = PackageManager.PERMISSION_GRANTED
        return ContextCompat.checkSelfPermission(context, permission) == granted
    }


    private fun showRostovLocation() {
        mapView.map.move(
            CameraPosition(Point(47.222078, 39.720358), 14.0f, 0.0f, 0.0f),
            Animation(Animation.Type.LINEAR, 0f),
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
                showMyLocationIconOnMap(currentPosition)
                mapView.map.move(
                    CameraPosition(currentPosition, 14.0f, 0.0f, 0.0f),
                    Animation(Animation.Type.SMOOTH, 0f),
                    null
                )
            }
            override fun onLocationStatusUpdated(locationStatus: LocationStatus) {
                // Обработка обновления статуса геолокации, если требуется
            }
        })
    }

    private fun showMyLocationIconOnMap(position: Point) {
        mapView.map.mapObjects.clear()

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

    companion object {

        @JvmStatic
        fun newInstance() =
            MapFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }



}