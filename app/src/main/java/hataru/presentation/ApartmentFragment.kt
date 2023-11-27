package com.example.listrooms.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.hataru.R
import hataru.domain.Apartment

class ApartmentFragment : Fragment() {

    private lateinit var viewModel: ApartmentViewModel

    private lateinit var twfAddress: TextView
    private lateinit var twfArea: TextView
    private lateinit var twfGuests: TextView
    private lateinit var twfPrice: TextView

    private var apartmentId = Apartment.UNDEFIND_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_apartment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ApartmentViewModel::class.java]
        initViews(view)
        launch()
    }

    private fun launch() {
        viewModel.getApartment(apartmentId)
        viewModel.apartment.observe(viewLifecycleOwner) {
            twfAddress.text = it.address
            twfArea.text = it.area.toString()
            twfGuests.text = it.people.toString()
            twfPrice.text = it.price.toString()
        }
    }

    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(APARTMENT_ID)) {
            throw RuntimeException("Param apartment id is abdent")
        }
        apartmentId = args.getInt(APARTMENT_ID, Apartment.UNDEFIND_ID)
    }

    private fun initViews(view: View) {
        twfAddress = view.findViewById<TextView>(R.id.text_View_Address_Full)
        twfArea = view.findViewById<TextView>(R.id.text_View_Area_Full)
        twfGuests = view.findViewById<TextView>(R.id.text_View_Guests_Full)
        twfPrice = view.findViewById<TextView>(R.id.text_View_Price_Full)
    }

    companion object {

        private const val APARTMENT_ID = "APARTMENT_ID"

        fun newInstanceItem(apartmentId: Int): ApartmentFragment {
            return ApartmentFragment().apply {
                arguments = Bundle().apply {
                    putInt(APARTMENT_ID, apartmentId)
                }
            }
        }
    }
}