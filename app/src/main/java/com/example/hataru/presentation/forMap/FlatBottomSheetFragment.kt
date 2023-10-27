package com.example.hataru.presentation.forMap

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hataru.R
import com.example.hataru.databinding.FragmentFlatBottomSheetBinding
import com.example.hataru.databinding.FragmentMapBinding
import com.example.hataru.presentation.showToast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class FlatBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding : FragmentFlatBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFlatBottomSheetBinding.inflate(inflater, container, false)


        // Handle actions inside the Bottom Sheet if needed
        

        return binding.root
    }


}