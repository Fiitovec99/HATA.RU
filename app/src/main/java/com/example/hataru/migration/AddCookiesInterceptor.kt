package com.example.hataru.migration

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class AddCookiesInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val requestBuilder = original.newBuilder()
            .header("Content-Type", "application/json")

        val preferences = context.getSharedPreferences("YOUR_PREF_NAME", Context.MODE_PRIVATE)
        val cookies = preferences.getStringSet("PREF_COOKIES", HashSet())

        for (cookie in cookies.orEmpty()) {
            requestBuilder.addHeader("Cookie", cookie)
        }

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}

