package com.example.hataru.presentation.migration
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface YourApiService {
    @POST("/")
    fun registerUser(@Body loginRequest: LoginRequest): Call<ResponseBody>
}