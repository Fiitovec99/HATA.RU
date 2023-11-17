package com.example.hataru.presentation.migration

import retrofit2.Call
import retrofit2.http.GET

interface HotelApi {
    @GET("/room")
    fun getRooms(): Call<RoomResponse>
}

