package com.example.hataru

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.viewpager.widget.ViewPager
import com.example.hataru.R
import com.example.hataru.presentation.onboarding.screens.ThirdScreen
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.hataru.presentation.onboarding.ViewPagerFragment
import com.example.hataru.presentation.onboarding.screens.FirstScreen

class OnboardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
    }
}