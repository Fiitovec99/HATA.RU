package com.example.hataru.presentation

import android.app.Application
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatDelegate
import com.example.hataru.BuildConfig
import com.example.hataru.InternetCheckService
import com.example.hataru.SharedPreferenceManger
import com.yandex.mapkit.MapKitFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    private val broadcastReceiver: InternetCheckService by lazy { InternetCheckService() }

    override fun onCreate() {
        super.onCreate()



        registerReceiver(
            broadcastReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )


        val sharedPreferenceManger = SharedPreferenceManger(this)
        AppCompatDelegate.setDefaultNightMode(sharedPreferenceManger.themeFlag[sharedPreferenceManger.theme])

        val API_KEY = BuildConfig.MAP_API_KEY
        MapKitFactory.setApiKey(API_KEY)


        startKoin {
            androidContext(this@App)
            modules(listOf(appMod,appModule) )
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        unregisterReceiver(broadcastReceiver)
    }



}