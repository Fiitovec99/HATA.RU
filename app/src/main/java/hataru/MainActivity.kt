package hataru


import android.content.res.Configuration
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log

import android.widget.TextView
import android.widget.Toast

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController

import com.example.hataru.R


import hataru.data.flatsContainer.roomTypes

//import com.example.hataru.presentation.migration.flatsContainer.roomTypes TODO

import com.google.android.material.bottomnavigation.BottomNavigationView

import com.yandex.mapkit.MapKitFactory

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import hataru.presentation.OnboardingActivity
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class MainActivity : AppCompatActivity() {

    private val API_KEY = "0fbc6a26-ede5-4bec-8b40-ec2e3ea8b780"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (flag){
            flag = false
            val intent = Intent(this, OnboardingActivity::class.java)
            startActivity(intent)
        }


        setNavigation()

        MapKitFactory.setApiKey(API_KEY)
        MapKitFactory.initialize(this)
       
//        MapKitFactory.setApiKey(API_KEY)
//        MapKitFactory.initialize(this)



//        apiService.authenticateUser(credentials = UserCredentials("6557acdcf08a9+14325@customapp.bnovo.ru", "f56c2ee76f46fa61")).enqueue(object :
//            Callback<Void> {
//            override fun onResponse(call: Call<Void>, response: Response<Void>) {
//                if (response.isSuccessful) {
//                    //val cookies: List<String> = response.headers().values("Cookie")
//
//                    //Log.d("API_RESPONSE", "Response: ${cookies.joinToString(", ")}")
//                    apiService.getRoomTypes().enqueue(object : Callback<Roomtypes> {
//                        override fun onResponse(call: Call<Roomtypes>, roomTypeResponse: Response<Roomtypes>) {
//                            if (roomTypeResponse.isSuccessful) {
//                                val rawResponse = roomTypeResponse.raw()
//                                val roomTypes = roomTypeResponse.body()
//                                Log.d("API_RESPONSE", "${roomTypeResponse.raw().request}")
//                                Log.d("API_RESPONSE", "Response: ${roomTypeResponse.code()} ${roomTypeResponse.message()} ${roomTypeResponse.raw().body}")
//                                Toast.makeText(this@MainActivity,roomTypes.toString(), Toast.LENGTH_LONG).show()
//                            }
//                        }
//
//                        override fun onFailure(call: Call<Roomtypes>, t: Throwable) {
//                            Log.e("API_RESPONSE","Authentication failed")
//                        }
//                    })
//                }
//            }
//            override fun onFailure(call: Call<Void>, t: Throwable) {
//                Log.e("API_RESPONSE","Authentication failed")
//            }
//        })



    }

    override fun onResume() {
        Log.d("TAG",roomTypes.size.toString())
        super.onResume()
    }

    private fun setNavigation(){
        val navView: BottomNavigationView = findViewById(R.id.bottom_navigation_view)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        setupWithNavController(navView,navController)
        setupWithNavController(navView, navController)
    }

    companion object{
        private var flag = true
    }

}





