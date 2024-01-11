package com.example.hataru.data


import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.example.hataru.migration.ApiService
import com.example.hataru.migration.MyCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.net.CookiePolicy


object ApiClient {

    val BASE_URL = "https://online.bnovo.ru"

    val gson: Gson = GsonBuilder().setLenient().create()

    private val cookieManager = CookieManager().apply{
        this.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
    }

    val loggingInterceptor = HttpLoggingInterceptor().apply {
        this.setLevel(HttpLoggingInterceptor.Level.HEADERS)
    }

    var client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .cookieJar(MyCookieJar())
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()



    val apiService: ApiService = retrofit.create(ApiService::class.java)

}



