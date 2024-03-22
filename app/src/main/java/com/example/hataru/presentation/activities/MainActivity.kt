package com.example.hataru.presentation.activities



import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.hataru.BuildConfig
import com.example.hataru.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yandex.mapkit.MapKitFactory


class MainActivity : AppCompatActivity() {

     val API_KEY = BuildConfig.MAP_API_KEY
    private var isMapKitInitialized = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setNavigation()


        if (savedInstanceState == null) {
            // Устанавливаем API ключ и инициализируем MapKit только если savedInstanceState равно null
            MapKitFactory.setApiKey(API_KEY)
            MapKitFactory.initialize(this)
        }


    }


    private fun setNavigation() {
        val navView: BottomNavigationView = findViewById(R.id.bottom_navigation_view)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        setupWithNavController(navView, navController)
        setupWithNavController(navView, navController)
    }


}





