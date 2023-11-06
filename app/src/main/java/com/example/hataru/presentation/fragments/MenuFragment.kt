package com.example.hataru.presentation.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hataru.R
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
        binding.textViewOpenWhatsUp.setOnClickListener {
            val phoneNumber = "+89959890049"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://api.whatsapp.com/send?phone=$phoneNumber")

            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText( activity as AppCompatActivity,"WhatsApp не установлен.", Toast.LENGTH_SHORT).show()
            }
        }
        }



    }

