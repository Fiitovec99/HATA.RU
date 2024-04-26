package com.example.hataru.presentation.activities


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.hataru.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setNavigation()
        setBottomNavigationListener()
        updateBottomNavigationState()
    }


    private fun setNavigation() {
        val navView: BottomNavigationView = findViewById(R.id.bottom_navigation_view)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        setupWithNavController(navView, navController)
    }

    private fun setBottomNavigationListener() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            val navController =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
            when (item.itemId) {
                R.id.listFlatsFragment -> {
                    navController.navController.navigate(R.id.listFlatsFragment)
                    true
                }

                R.id.mapFragment -> {
                    navController.navController.navigate(R.id.mapFragment)
                    true
                }

                R.id.favoriteFlatFragment -> {
                    navController.navController.navigate(R.id.favoriteFlatFragment)
                    true
                }

                R.id.menuFragment -> {
                    navController.navController.navigate(R.id.menuFragment)
                    true
                }

                else -> false
            }
        }
    }

    private fun updateBottomNavigationState() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        val navController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val currentDestination = navController.navController.currentDestination?.id
        when (currentDestination) {
            R.id.listFlatsFragment -> bottomNavigationView.selectedItemId = R.id.listFlatsFragment
            R.id.mapFragment -> bottomNavigationView.selectedItemId = R.id.mapFragment
            R.id.favoriteFlatFragment -> bottomNavigationView.selectedItemId = R.id.favoriteFlatFragment
            R.id.menuFragment -> bottomNavigationView.selectedItemId = R.id.menuFragment
        }
    }


}





