package com.example.hataru.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hataru.R
import com.example.hataru.databinding.FragmentFlatBinding
import com.example.hataru.domain.entity.Roomtypes
import com.example.hataru.presentation.fragments.FlatBottomSheetFragment.Companion.KEY_GET_FLAT

class FlatFragment : Fragment() {


    private lateinit var binding : FragmentFlatBinding
    private lateinit var flat : Roomtypes

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFlatBinding.inflate(layoutInflater,container,false)
        flat = arguments?.getSerializable(KEY_GET_FLAT_INTO_FLATFRAGMENT) as Roomtypes

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            textFlatPrice.text = "Цена аренды: " + flat.price!!.toInt().toString() + "р"
            textFlatLocation.text = flat.address
            exampleOfFlatImageView.setImageResource(R.drawable.example_of_flat_bottom_fragment)
            countAdultsFlatTextView.text = "Количество взрослых: "+ flat.adults.toString()
            countChildrenFlatTextView.text = "Количество детей: " + flat.children.toString()


        }


    }

    companion object{

        const val KEY_GET_FLAT_INTO_FLATFRAGMENT = "KEY_GET_FLAT"
    }



}