package com.example.listrooms.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.example.hataru.R
import hataru.domain.Apartment

class ApartmentActivity : FragmentActivity() {

    private var apartmentId = Apartment.UNDEFIND_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apartment)
        parseIntent()
        if (savedInstanceState == null) {
            launch()
        }
    }

    private fun launch() {
        val fragment = ApartmentFragment.newInstanceItem(apartmentId)
        supportFragmentManager.beginTransaction()
            .replace(R.id.apartment_container, fragment)
            .commit()
        }

    private fun parseIntent() {
        if (!intent.hasExtra(APARTMENT_ID)) {
            throw RuntimeException("Param apartment id is abdent")
        }
        apartmentId = intent.getIntExtra(APARTMENT_ID, Apartment.UNDEFIND_ID)
    }

    companion object {

        private const val   APARTMENT_ID = "APARTMENT_ID"

        fun newIntent (context: Context, apartmentId: Int): Intent {
            val intent = Intent(context, ApartmentActivity::class.java)
            intent.putExtra(APARTMENT_ID, apartmentId)
            return intent
        }
    }
}