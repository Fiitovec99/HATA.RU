package com.example.hataru.presentation.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.hataru.R
import com.example.hataru.presentation.activities.MainActivity
import com.google.firebase.auth.FirebaseAuth

class SplashFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        firebaseAuth = FirebaseAuth.getInstance()
        val mainHandler = Handler(Looper.getMainLooper())
        val runnable = Runnable {
            if (oBoardingFinished()){
                if(firebaseAuth.currentUser!=null){
                    moveToMainActivity()
                }else{
                    findNavController().navigate(R.id.action_splashFragment_to_logInFragment)
                }
            } else {
                if(firebaseAuth.currentUser!=null){
                    moveToMainActivity()
                }else{
                    findNavController().navigate(R.id.action_splashFragment_to_viewPagerFragment)
                }

            }
        }

        mainHandler.postDelayed(runnable, 0) //TODO поставить 1500

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    private fun moveToMainActivity(){
        findNavController().navigate(R.id.action_splashFragment_to_mainActivity)
        requireActivity().finish()
    }

    private fun oBoardingFinished(): Boolean{
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }
}