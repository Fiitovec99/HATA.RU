package com.example.hataru.presentation

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment



    fun Fragment.showToast(s: String) {
        Toast.makeText(activity, s, Toast.LENGTH_SHORT).show()
    }

    fun AppCompatActivity.showToast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    fun Fragment.showAlertDialog(context: Context,text : String){
        val longText = text
        val alertDialog = AlertDialog.Builder(context)
            .setMessage(longText)
            .setPositiveButton("ОК") { dialog, _ -> dialog.dismiss() }
            .create()
        alertDialog.show()
    }