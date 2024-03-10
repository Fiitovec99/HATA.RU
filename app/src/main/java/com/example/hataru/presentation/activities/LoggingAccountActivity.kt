package com.example.hataru.presentation.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hataru.R
import com.example.hataru.presentation.ui.login.LoginFragment
import com.google.firebase.auth.FirebaseAuth

class LoggingAccountActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logging_account)

        firebaseAuth = FirebaseAuth.getInstance()

        val fragmentManager = supportFragmentManager

        val transaction = fragmentManager.beginTransaction().replace(R.id.fragmentContainer,LoginFragment()).commit()







    }


}