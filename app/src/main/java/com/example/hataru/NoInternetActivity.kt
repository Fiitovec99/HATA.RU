package com.example.hataru


import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.hataru.presentation.activities.OnboardingActivity



class NoInternetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_dialog)

        val retryButton: Button = findViewById(R.id.retry)
        val animationView: LottieAnimationView = findViewById(R.id.animationView)
        retryButton.setOnClickListener {

            val isConnected = isInternetConnected(applicationContext)
            if (isConnected) {

                animationView.setAnimation(R.raw.internet_yes)
                animationView.playAnimation()

                val delayMillis = 2800L
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(this, OnboardingActivity::class.java)
                    startActivity(intent)
                    finish()
                }, delayMillis)

            } else {
                showToast("Интернет не обнаружен!")
            }
        }
    }


    private fun isInternetConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}
