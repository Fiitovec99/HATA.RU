package com.example.hataru.presentation.onboarding.screens
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hataru.R
import com.example.hataru.databinding.FragmentFirstScreenBinding

class FirstScreen : Fragment() {
    private lateinit var binding: FragmentFirstScreenBinding
    private var currentMessageIndex = 0
    private val messages = mutableListOf<View>()
    private val handler = Handler(Looper.getMainLooper())

    private val messageDisplayRunnable = object : Runnable {
        override fun run() {
            if (currentMessageIndex < messages.size) {
                showNextMessage()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstScreenBinding.inflate(inflater, container, false)
        messages.add(binding.message2)
        messages.add(binding.message3)
        messages.add(binding.message4)
        handler.postDelayed(messageDisplayRunnable, 4000)
        binding.constraintLayoutContainer.setOnClickListener {
            showNextMessage()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.skip.setOnClickListener {
            navigateToLogIn()
        }
    }

    private fun showNextMessage() {
        if (currentMessageIndex < messages.size) {
            handler.removeCallbacks(messageDisplayRunnable)
            val nextMessage = messages[currentMessageIndex]
            nextMessage.visibility = View.VISIBLE
            nextMessage.startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_in))
            currentMessageIndex++
            if (currentMessageIndex < messages.size) {
                handler.postDelayed(messageDisplayRunnable, 4000)
            }
        }
    }

    private fun navigateToLogIn() {
        findNavController().navigate(R.id.logInFragment)
        onBoardingFinished()
    }

    private fun onBoardingFinished() {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("Finished", true)
            apply()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
    }
}