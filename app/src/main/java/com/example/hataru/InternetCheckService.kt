package com.example.hataru

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Handler
import android.os.Looper
import com.example.hataru.presentation.activities.NoInternetActivity

class InternetCheckService : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo

        if (networkInfo != null && networkInfo.isConnected) {
            // Toast.makeText(context, "Интернет подключен", Toast.LENGTH_SHORT).show() ИНЕТ ЕСТЬ
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

