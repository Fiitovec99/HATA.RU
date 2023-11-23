package com.example.hataru.presentation.fragments

import android.app.Fragment
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hataru.R
import com.example.hataru.databinding.FragmentFavoriteFlatBinding
import com.example.hataru.databinding.FragmentMenuBinding

class FavoriteFlatFragment : Fragment() {

    //    companion object {
//        fun newInstance() = FavoriteFlatFragment()
//    }
//
//    private lateinit var viewModel: FavoriteFlatViewModel
    private lateinit var binding: FragmentFavoriteFlatBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteFlatBinding.inflate(inflater, container, false)
        return binding.root
    }
    fun ShowToast(){
        Toast.makeText(activity as AppCompatActivity, "в разработке",Toast.LENGTH_SHORT)
                .show()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.apply {
            imageViewApartment.setOnClickListener {
                ShowToast()
            }
            imageViewApartment1.setOnClickListener {
                ShowToast()
            }
            imageViewApartment2.setOnClickListener {
                ShowToast()
            }
            imageViewApartment3.setOnClickListener {
                ShowToast()
            }
            imageViewApartment4.setOnClickListener {
                ShowToast()
            }
            imageViewApartment5.setOnClickListener {
                ShowToast()
            }
        }
    }
}
