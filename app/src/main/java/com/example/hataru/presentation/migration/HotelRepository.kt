package com.example.hataru.presentation.migration

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HotelRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://online.bnovo.ru")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(HotelApi::class.java)

    fun getRooms(): RoomResponse? {
        val call = api.getRooms()
        val response = call.execute()
        if (response.isSuccessful) {
            return response.body()
        }
        return null
    }
}
