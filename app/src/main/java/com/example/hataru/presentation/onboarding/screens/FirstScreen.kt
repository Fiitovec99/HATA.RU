package com.example.hataru.presentation.onboarding.screens

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.hataru.R
import com.example.hataru.databinding.FragmentFirstScreenBinding
import com.example.hataru.databinding.FragmentViewPagerBinding
import com.example.hataru.presentation.onboarding.ViewPagerAdapter

class FirstScreen : Fragment() {
    private lateinit var binding: FragmentFirstScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFirstScreenBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.message1.visibility = View.VISIBLE
        binding.message1.startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_in))

        binding.message1.setOnClickListener {
            binding.message2.visibility = View.VISIBLE
            binding.message2.startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_in))
        }

        binding.message2.setOnClickListener {
            binding.message3.visibility = View.VISIBLE
            binding.message3.startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_in))
        }

        binding.message3.setOnClickListener {
            binding.message4.visibility = View.VISIBLE
            binding.message4.startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_in))
        }

        binding.message4.setOnClickListener {
            findNavController().navigate(R.id.logInFragment)
            onBoardingFinished()
        }

        return view
    }

    private fun onBoardingFinished(){
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished", true)
        editor.apply()
    }
}