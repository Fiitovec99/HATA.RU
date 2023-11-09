package com.example.hataru.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupWithNavController

import com.example.hataru.R
import com.example.hataru.presentation.migration.ApiClient
import com.example.hataru.presentation.migration.GetRoomsUseCase
import com.example.hataru.presentation.migration.HotelRepository
import com.example.hataru.presentation.migration.LoginRequest
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.Serializable

class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.bottom_navigation_view)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        setupWithNavController(navView,navController)


        val login = "65156a39ef62e+14325@customapp.bnovo.ru"
        val password = "109bbf24e8d8790c"

        val apiService = ApiClient.apiService
        val loginRequest = LoginRequest(login, password)
        val call = apiService.registerUser(loginRequest)
        val hotelRepository = HotelRepository()

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val code = response.code()
                if (code ==200) {
                    Toast.makeText(applicationContext,"asd", Toast.LENGTH_LONG).show()
                    // Регистрация прошла успешно, сервер вернул код 2xx
                    Toast.makeText(applicationContext, "Регистрация прошла успешно!", Toast.LENGTH_LONG).show()
                } else {
                    // Регистрация не удалась, сервер вернул другой код
                    Toast.makeText(applicationContext, "Не удалось зарегистрироваться. Код: $code", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Обработка ошибок сети или других ошибок
                Toast.makeText(applicationContext, "Ошибка: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })




    }




}




