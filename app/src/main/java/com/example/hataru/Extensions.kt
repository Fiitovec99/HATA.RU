package com.example.hataru

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment



    fun Fragment.showToast(s: String) {
        Toast.makeText(activity, s, Toast.LENGTH_SHORT).show()
    }

    fun AppCompatActivity.showToast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    fun showAlertDialog(context: Context,text : String){
        val longText = text
        val alertDialog = AlertDialog.Builder(context)
            .setMessage(longText)
            .setPositiveButton("ОК") { dialog, _ -> dialog.dismiss() }
            .create()
        alertDialog.show()
    }

     fun isLocationEnabled(context: Context): Boolean {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        val granted = PackageManager.PERMISSION_GRANTED
        return ContextCompat.checkSelfPermission(context, permission) == granted
    }

    fun GetDescriptionFlat(s : String) : List<String>{
        val regex = Regex("""[^:]+:\s*"([^"]+)"""")
        val matches = regex.findAll(s)
        return matches.map { it.groupValues[1] }.toList()
    }