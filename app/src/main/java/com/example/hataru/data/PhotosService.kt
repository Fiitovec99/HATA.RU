package com.example.hataru.data

import com.example.hataru.domain.entity.Photos
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface PhotosService {

    @GET("roomtypes?account_id=10449")
    suspend fun getPhotos() : Response<Photos>
}