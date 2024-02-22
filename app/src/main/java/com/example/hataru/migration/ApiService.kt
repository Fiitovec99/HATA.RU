package com.example.hataru.migration

import com.example.hataru.domain.entity.Photos
import com.example.hataru.domain.entity.Roomtype
import com.example.hataru.domain.entity.Roomtypes
import com.example.hataru.domain.entity.Root
import com.example.hataru.domain.entity.UserCredentials
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST



interface ApiService {

    @POST("/")
    fun authenticateUser(
        @Header("Accept") acceptHeader: String = "application/json",
        @Header("Content-Type") contentTypeHeader: String = "application/json",
        @Body credentials: UserCredentials,
    ): Call<Void>


    @GET("/roomTypes/get")
    fun getRoomTypes(
        @Header("Content-Type") contentTypeHeader: String = "application/json",
        @Header("Accept") acceptHeader: String = "application/json",
    ): Call<Root>






}
