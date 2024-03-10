package com.example.hataru.domain.entity

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class ReceivedCookiesInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            val cookies = HashSet<String>()

            for (header in originalResponse.headers("Set-Cookie")) {
                cookies.add(header)
            }

            val preferences = context.getSharedPreferences("YOUR_PREF_NAME", Context.MODE_PRIVATE)
            preferences.edit().putStringSet("PREF_COOKIES", cookies).apply()
        }

        return originalResponse
    }
}


