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
import com.example.hataru.presentation.activities.MainActivity
import com.example.hataru.R
import com.example.hataru.databinding.FragmentLoginBinding
import com.example.hataru.databinding.FragmentLoginNewBinding
import com.example.hataru.showToast
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var loginViewModel: LoginViewModel
    private var _binding: FragmentLoginNewBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firebaseAuth = FirebaseAuth.getInstance()

        _binding = FragmentLoginNewBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        if(firebaseAuth.currentUser!=null){
//            val intent = Intent(requireContext(), MainActivity::class.java)
//            startActivity(intent)
//        }

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        val usernameEditText = binding.username
        val passwordEditText = binding.password
        val confirmPasswordEditText = binding.confirmPassword
        val loginButton = binding.login
        val loadingProgressBar = binding.loading

        loginViewModel.loginFormState.observe(viewLifecycleOwner,
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

        loginViewModel.loginResult.observe(viewLifecycleOwner,
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





        _binding?.textViewHasAccount?.setOnClickListener {
            findNavController().navigate(R.id.action_logInFragment_to_logUpFragment)
        }



        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                loginViewModel.loginDataChanged(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
        }
        usernameEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)
        confirmPasswordEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
            false
        }

        _binding!!.login.setOnClickListener {

            if(usernameEditText.text.toString()=="" || passwordEditText.text.toString()=="" || confirmPasswordEditText.text.toString()==""){
                showToast("Вы не заполнили одно из полей")
                return@setOnClickListener
            }

            if(passwordEditText.text.toString() != confirmPasswordEditText.text.toString()) {
                showToast("Пароли не совпадают")
                return@setOnClickListener
            }

            loadingProgressBar.visibility = View.VISIBLE
            firebaseAuth.createUserWithEmailAndPassword(
                usernameEditText.text.toString(),
                passwordEditText.text.toString()
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Регистрация успешна, вы можете выполнить необходимые действия
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                } else {
                    // Регистрация не удалась, отобразить сообщение об ошибке
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