package com.example.hataru

import android.app.Activity
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import com.example.hataru.presentation.activities.MainActivity
import com.example.hataru.presentation.activities.OnboardingActivity
import com.google.firebase.auth.FirebaseAuth

class InternetCheckService : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo

        if (networkInfo != null && networkInfo.isConnected) {
            Toast.makeText(context, "Интернет подключен", Toast.LENGTH_SHORT).show()
        } else {
            showNoInternetDialog(context.applicationContext)
        }
    }

    private fun showNoInternetDialog(context: Context) {
        Handler(Looper.getMainLooper()).post {
            val intent = Intent(context, NoInternetActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }





}

