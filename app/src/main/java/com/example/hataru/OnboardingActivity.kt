package com.example.hataru

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

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