package com.example.hataru.presentation.migration


import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiClient {


    val BASE_URL = "https://online.bnovo.ru"



    val gson: Gson = GsonBuilder().setLenient().create()

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val retrofitInterface = retrofit.create(ApiService::class.java)

    val apiService: ApiService = retrofit.create(ApiService::class.java)

    var client = OkHttpClient.Builder()
        .cookieJar(MyCookieJar())
        .build()


}
object SessionManager {
    var sid: String? = null
}


