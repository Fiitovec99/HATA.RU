package com.example.hataru.presentation.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hataru.R
import com.example.hataru.databinding.FragmentForgotPasswordBinding
import com.example.hataru.showToast
import com.google.firebase.auth.FirebaseAuth


class ForgotPassword : Fragment() {


    private lateinit var firebaseAuth: FirebaseAuth

    private var _binding: FragmentForgotPasswordBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firebaseAuth = FirebaseAuth.getInstance()

        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val loadingProgressBar = binding.loading



        binding.continue4.setOnClickListener{
            if(_binding!!.textViewForgotPassword.text.toString() == ""){
                showToast("Пусто")
                return@setOnClickListener
            }

            loadingProgressBar.visibility = View.VISIBLE

            firebaseAuth.sendPasswordResetEmail(
                binding.textViewForgotPassword.text.toString()
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToast("На вашу почту отправлено сообщение для снятия пароля")
                } else {
                    showToast("Ошибка")
                    Toast.makeText(context, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
                loadingProgressBar.visibility = View.GONE
            }
        }








    }





    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome) + model.displayName
        // TODO : initiate successful logged in experience
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()

    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
//        if(firebaseAuth.currentUser != null){
//            val intent = Intent(context,MainActivity::class.java)
//            startActivity(intent)
//        }

    }



}