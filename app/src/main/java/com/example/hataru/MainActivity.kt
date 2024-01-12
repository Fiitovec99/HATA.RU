package com.example.hataru



import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.hataru.data.ApiClient.apiService
import com.example.hataru.data.GeometryProvider.listFlats
import com.example.hataru.data.flatsContainer
import com.example.hataru.domain.entity.Roomtype
import com.example.hataru.domain.entity.Roomtypes

import com.example.hataru.domain.entity.Root
import com.example.hataru.domain.entity.UserCredentials
import com.example.hataru.migration.MyCookieJar
import com.example.hataru.presentation.fragments.flats
import com.example.hataru.presentation.fragments.points
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private val API_KEY = "0fbc6a26-ede5-4bec-8b40-ec2e3ea8b780"
    private var isMapKitInitialized = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        MapKitFactory.setApiKey(API_KEY)
        if (!isMapKitInitialized) {
            MapKitFactory.initialize(this)
            isMapKitInitialized = true
        }
        setNavigation()


//        MapKitFactory.setApiKey(API_KEY)
//        MapKitFactory.initialize(this)

        val myCookieJar = MyCookieJar()

        apiService.authenticateUser(
            credentials = UserCredentials(
                "",
                ""
            )
        ).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("asdasd", response.body().toString())


                    apiService.getRoomTypes().enqueue(object : Callback<Root> {
                        override fun onResponse(call: Call<Root>, roomTypeResponse: Response<Root>) {
                            if (roomTypeResponse.isSuccessful) {

                                val flat = roomTypeResponse.body() as Root
                                listFlats = flat.roomtypes!!.toList()
                                Log.d("URAAAAAA",flat.roomtypes!!.joinToString(", "))

                                Log.d("Asdasdfaefg", flat.roomtypes!!.size.toString())

                                for(i in 0..36){
                                    Log.d("flat " +i.toString(), listFlats[i].toString())
                                }

                                flats = flat.roomtypes!!
                                points = flats.map { x : Roomtype -> Point(x.geo_data!!.x!!.toDouble(),
                                    x!!.geo_data!!.y!!.toDouble()) }

                            }
                        }

                        override fun onFailure(call: Call<Root>, t: Throwable) {
                            Log.e("API_FAILURE", "Error: ${t.message}", t)
                            Toast.makeText(
                                this@MainActivity,
                                "Ошибка: ${t.message}",
                                Toast.LENGTH_LONG
                            ).show()

                        }
                    })


                }

            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("API_RESPONSE", "Authentication failed")


            }
        })







    }

    override fun onResume() {
        //Log.d("TAG",roomTypes.size.toString())
        super.onResume()
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





