package com.example.hataru

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate

class SharedPreferenceManger(context: Context) {
    private val preference = context.getSharedPreferences(
        context.packageName,
        Context.MODE_PRIVATE
    )
    private val editor = preference.edit()

    private val keyTheme = "theme"


    var theme
        get() = preference.getInt(keyTheme, 0) // по дефолту белая тема
        set(value) {
            editor.putInt(keyTheme, value)
            editor.commit()
        }

    init {
        Log.d("theme", theme.toString())
    }

    val themeFlag = arrayOf(
        AppCompatDelegate.MODE_NIGHT_NO,
        AppCompatDelegate.MODE_NIGHT_YES,
        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    )
}

