package com.example.hataru.presentation.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hataru.databinding.FragmentViewPagerBinding
import com.example.hataru.presentation.onboarding.screens.FirstScreen

class ViewPagerFragment : Fragment() {


    private lateinit var binding: FragmentViewPagerBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewPagerBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        val view = binding.root

        val fragmentList = arrayListOf<Fragment>(
            FirstScreen()
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.viewPager.adapter = adapter

//        val springDotsIndicator = binding.wormDotsIndicator
//        springDotsIndicator.attachTo(binding.viewPager)


        return view
    }
}