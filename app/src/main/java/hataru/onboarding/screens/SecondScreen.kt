package com.example.hataru.presentation.onboarding.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.hataru.R
import com.example.hataru.databinding.FragmentFirstScreenBinding
import com.example.hataru.databinding.FragmentSecondScreenBinding
import com.example.hataru.databinding.FragmentThirdScreenBinding

class SecondScreen : Fragment() {
    private lateinit var binding: FragmentSecondScreenBinding

    private val navigateToMainActivity = MutableLiveData<Boolean>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSecondScreenBinding.inflate(layoutInflater)
        val view = binding.root

        binding.continue2.setOnClickListener {
            findNavController().navigate(R.id.mainActivity)
            navigateToMainActivity.value = true
        }

        return view
    }

    fun getNavigateToMainActivity(): LiveData<Boolean> {
        return navigateToMainActivity
    }
}