package com.example.hataru.presentation.forMap

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.hataru.R
import com.example.hataru.databinding.FragmentFlatBottomSheetBinding
import com.example.hataru.databinding.FragmentMapBinding
import com.example.hataru.presentation.showToast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val COLLAPSED_HEIGHT = 228
class FlatBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding : FragmentFlatBottomSheetBinding

   private  var flatId : Int = 0
    private  var flatCost : Double = 0.0



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFlatBottomSheetBinding.inflate(inflater, container, false)

        flatId = arguments?.getInt("id", -1) ?: -1
        flatCost = arguments?.getDouble("cost", 0.0) ?: 0.0
        //TODO

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            textFlatPrice.text = flatCost.toString()

        }


    }

    override fun onStart() {
        super.onStart()


        }

    }



