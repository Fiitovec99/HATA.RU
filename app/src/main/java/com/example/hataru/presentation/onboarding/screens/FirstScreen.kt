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
    ): View? {
        binding = FragmentFirstScreenBinding.inflate(inflater, container, false)
        val view = binding.root

        messages.add(binding.message1)
        messages.add(binding.message2)
        messages.add(binding.message3)
        messages.add(binding.message4)

        handler.postDelayed(messageDisplayRunnable, 3000) // Начинаем с первого сообщения

        binding.constraintLayoutContainer.setOnClickListener {
            handler.removeCallbacks(messageDisplayRunnable) // Останавливаем автопоявление
            showNextMessage() // Показываем следующее сообщение
// Перезапускаем автопоявление для следующего сообщения, если оно не последнее
            if (currentMessageIndex < messages.size) {
                handler.postDelayed(messageDisplayRunnable, 3000)
            }
        }

        return view
    }

    private fun showNextMessage() {
        if (currentMessageIndex < messages.size) {
            val nextMessage = messages[currentMessageIndex]
            nextMessage.visibility = View.VISIBLE
            nextMessage.startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_in))
            currentMessageIndex++
// Если это последнее сообщение, планируем переход через 5 секунд
            if (currentMessageIndex == messages.size) {
                handler.postDelayed({
                    findNavController().navigate(R.id.logInFragment)
                    onBoardingFinished()
                }, 5000) // Задержка перед переходом
            }
        }
    }

    private fun onBoardingFinished() {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished", true)
        editor.apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null) // Очищаем все задания
    }
}