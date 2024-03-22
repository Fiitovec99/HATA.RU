package com.example.hataru.presentation

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.hataru.SharedPreferenceManger
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val sharedPreferenceManger = SharedPreferenceManger(this)
        AppCompatDelegate.setDefaultNightMode(sharedPreferenceManger.themeFlag[sharedPreferenceManger.theme])

        startKoin {
            androidContext(this@App)
            modules(listOf(appMod,appModule) )
        }
    }

}