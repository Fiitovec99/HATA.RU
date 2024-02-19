package com.example.hataru

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.replace
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import com.example.hataru.presentation.ui.login.LogUpFragment
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