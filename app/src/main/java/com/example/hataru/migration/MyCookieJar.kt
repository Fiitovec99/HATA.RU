package com.example.hataru.migration

import android.util.Log
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl


class MyCookieJar : CookieJar {

    private var cookies: List<Cookie>? = null

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookies ?: ArrayList()
    }

    override fun saveFromResponse(url: HttpUrl, cookies1: List<Cookie>) {
        cookies =  cookies1;
    }
}