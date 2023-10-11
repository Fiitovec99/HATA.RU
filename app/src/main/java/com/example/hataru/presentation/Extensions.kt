package com.example.hataru.presentation

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment



    fun Fragment.showToast(s: String) {
        Toast.makeText(activity, s, Toast.LENGTH_SHORT).show()
    }

    fun AppCompatActivity.showToast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

