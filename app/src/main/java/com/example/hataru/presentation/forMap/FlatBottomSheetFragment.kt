package com.example.hataru.presentation.forMap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hataru.R
import com.example.hataru.databinding.FragmentFlatBottomSheetBinding
import com.example.hataru.presentation.migration.Roomtypes
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val COLLAPSED_HEIGHT = 228

class FlatBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentFlatBottomSheetBinding

    private lateinit var flat : Roomtypes



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFlatBottomSheetBinding.inflate(inflater, container, false)

        flat = arguments?.getSerializable("roomtypes") as Roomtypes

//        flatId = arguments?.getInt("id", -1) ?: -1
//        flatCost = arguments?.getInt("cost", 0) ?: 0
        //TODO

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            textFlatPrice.text = "Цена аренды: " + flat.price.toString()
            exampleOfFlatImageView.setImageResource(R.drawable.example_of_flat_bottom_fragment)

        }


    }

    override fun onStart() {
        super.onStart()


    }

}



