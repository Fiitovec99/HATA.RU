package com.example.hataru.presentation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hataru.R

class OnboardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.findFragmentByTag("logUpFragment") != null) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

}