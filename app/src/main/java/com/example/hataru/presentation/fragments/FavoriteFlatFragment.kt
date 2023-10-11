package com.example.hataru.presentation.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hataru.R

class FavoriteFlatFragment : Fragment() {

//    companion object {
//        fun newInstance() = FavoriteFlatFragment()
//    }
//
//    private lateinit var viewModel: FavoriteFlatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite_flat, container, false)
    }

}