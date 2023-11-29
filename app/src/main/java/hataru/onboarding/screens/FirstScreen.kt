package com.example.hataru.presentation.onboarding.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.hataru.R
import com.example.hataru.databinding.FragmentFirstScreenBinding
import com.example.hataru.databinding.FragmentThirdScreenBinding
import com.example.hataru.databinding.FragmentViewPagerBinding
import com.example.hataru.presentation.onboarding.ViewPagerAdapter

class FirstScreen : Fragment() {
    private lateinit var binding: FragmentFirstScreenBinding

    private val navigateToMainActivity = MutableLiveData<Boolean>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFirstScreenBinding.inflate(layoutInflater)
        val view = binding.root

        binding.continue1.setOnClickListener {
            findNavController().navigate(R.id.mainActivity)
            navigateToMainActivity.value = true
        }

        return view
    }

    fun getNavigateToMainActivity(): LiveData<Boolean> {
        return navigateToMainActivity
    }
}