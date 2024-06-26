package com.example.hataru.presentation.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hataru.R
import com.example.hataru.databinding.FragmentLogUpBinding
import com.example.hataru.databinding.FragmentLogUpNewBinding
import com.example.hataru.presentation.activities.MainActivity
import com.example.hataru.showToast
import com.google.firebase.auth.FirebaseAuth


class LogUpFragment : Fragment() {


    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var logUpViewModel: LogUpViewModel
    private var _binding: FragmentLogUpNewBinding? = null


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firebaseAuth = FirebaseAuth.getInstance()

        _binding = FragmentLogUpNewBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logUpViewModel = ViewModelProvider(this, LogUpViewModelFactory())
            .get(LogUpViewModel::class.java)

        val usernameEditText = binding.username
        val passwordEditText = binding.password
        val loginButton = binding.login
        val loadingProgressBar = binding.loading

        logUpViewModel.loginFormState.observe(viewLifecycleOwner,
            Observer { loginFormState ->
                if (loginFormState == null) {
                    return@Observer
                }
                loginButton.isEnabled = loginFormState.isDataValid
                loginFormState.usernameError?.let {
                    usernameEditText.error = getString(it)
                }
                loginFormState.passwordError?.let {
                    passwordEditText.error = getString(it)
                }
            })

        logUpViewModel.loginResult.observe(viewLifecycleOwner,
            Observer { loginResult ->
                loginResult ?: return@Observer
                loadingProgressBar.visibility = View.GONE
                loginResult.error?.let {
                    showLoginFailed(it)
                }
                loginResult.success?.let {
                    //updateUiWithUser(it)
                }

            })

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                logUpViewModel.loginDataChanged(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
        }
        usernameEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                logUpViewModel.login(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
            false
        }


        binding.textViewForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_logUpFragment_to_forgotPassword)
        }





        _binding!!.login.setOnClickListener {
            if(usernameEditText.text.toString()=="" || passwordEditText.text.toString()==""){
                showToast("Вы не заполнили одно из полей")
                return@setOnClickListener
            }
            loadingProgressBar.visibility = View.VISIBLE
            firebaseAuth.signInWithEmailAndPassword(
                usernameEditText.text.toString(),
                passwordEditText.text.toString()
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //TODO
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                } else {

                    Toast.makeText(context, task.exception?.message, Toast.LENGTH_SHORT).show()
                    Log.d("TAG", task.exception.toString())
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