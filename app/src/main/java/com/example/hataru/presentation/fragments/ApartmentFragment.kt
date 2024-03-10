package com.example.hataru.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.hataru.R
import com.example.hataru.domain.uselessUseCase.Apartment
import com.example.hataru.presentation.viewModels.ApartmentViewModel

class ApartmentFragment : Fragment() {

    private lateinit var viewModel: ApartmentViewModel

    private lateinit var twfAddress: TextView
    private lateinit var twfArea: TextView
    private lateinit var twfGuests: TextView
    private lateinit var twfPrice: TextView
    private lateinit var twfDescription: TextView

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
        twfAddress = view.findViewById(R.id.text_View_Address_Full)
        twfArea = view.findViewById(R.id.text_View_Area_Full)
        twfGuests = view.findViewById(R.id.text_View_Guests_Full)
        twfPrice = view.findViewById(R.id.text_View_Price_Full)
        twfDescription = view.findViewById(R.id.text_View_Description_Full)

        viewModel = ViewModelProvider(this)[ApartmentViewModel::class.java]
        initViews(view)
        launch()
    }

    private fun launch() {
        viewModel.getApartment(apartmentId)
        viewModel.apartment.observe(viewLifecycleOwner) {
            twfAddress.text = it.address
            twfArea.text = "Площадь: " + it.area.toString()
            twfGuests.text = "Кол-во гостей: " + it.people.toString()
            twfPrice.text = "Цена: " + it.price.toString() + "руб"
            twfDescription.text = "\n" + it.description
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
        twfAddress = view.findViewById(R.id.text_View_Address_Full)
        twfArea = view.findViewById(R.id.text_View_Area_Full)
        twfGuests = view.findViewById(R.id.text_View_Guests_Full)
        twfPrice = view.findViewById(R.id.text_View_Price_Full)
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