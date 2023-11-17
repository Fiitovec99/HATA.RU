package com.example.hataru.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController

import com.example.hataru.R
import com.example.hataru.presentation.migration.AddCookiesInterceptor
import com.example.hataru.presentation.migration.ApiClient.apiService

import com.example.hataru.presentation.migration.RoomType
import com.example.hataru.presentation.migration.UserCredentials

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private val login = "65156a39ef62e+14325@customapp.bnovo.ru"
    private val password = "109bbf24e8d8790c"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setNavigation()


        apiService.authenticateUser(credentials = UserCredentials(login, password)).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
//                    val cookies: List<String> = response.headers().values("Cookie")

                    //Log.d("API_RESPONSE", "Response: ${cookies.joinToString(", ")}")

                    apiService.getRoomTypes().enqueue(object : Callback<RoomType> {
                        override fun onResponse(call: Call<RoomType>, roomTypeResponse: Response<RoomType>) {
                            if (roomTypeResponse.isSuccessful) {
                                val rawResponse = roomTypeResponse.raw()
                                val roomTypes = roomTypeResponse.body()
                                Log.d("API_RESPONSE", "${roomTypeResponse.raw().request}")
                                Log.d("API_RESPONSE", "Response: ${roomTypeResponse.code()} ${roomTypeResponse.message()} ${roomTypeResponse.raw().body}")
                                Toast.makeText(this@MainActivity,roomTypes.toString(),Toast.LENGTH_LONG).show()
                            }
                        }

                        override fun onFailure(call: Call<RoomType>, t: Throwable) {
                            Log.e("API_RESPONSE","Authentication failed")
                        }
                    })
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("API_RESPONSE","Authentication failed")
            }
        })






    }

    private fun setNavigation(){
        val navView: BottomNavigationView = findViewById(R.id.bottom_navigation_view)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        setupWithNavController(navView, navController)
    }




}





