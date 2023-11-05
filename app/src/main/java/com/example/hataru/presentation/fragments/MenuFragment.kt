package com.example.hataru.presentation.fragments

import android.content.ClipData.newIntent
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hataru.databinding.FragmentMenuBinding


class MenuFragment : Fragment() {

//    companion object {
//        fun newInstance() = MenuFragment()
//    }
//
//    private lateinit var viewModel: MenuViewModel
    private lateinit var binding: FragmentMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textView.setOnClickListener {

            //Дальнейшая реализация логики слушателя
        }

        binding.LinkToTheWebsite.setOnClickListener{
            val browserIntent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse("hataru.ru"))
            startActivity(browserIntent)


        }

    }

}