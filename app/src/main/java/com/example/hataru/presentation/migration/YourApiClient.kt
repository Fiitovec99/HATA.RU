package com.example.hataru.presentation.migration

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class YourApiClient {

    companion object {
        private const val BASE_URL = "https://online.bnovo.ru/"

        fun create(): YourApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(YourApiService::class.java)
        }
    }
}