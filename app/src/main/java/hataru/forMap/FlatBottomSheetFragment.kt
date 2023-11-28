package hataru.forMap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hataru.R
import com.example.hataru.databinding.FragmentFlatBottomSheetBinding
import hataru.migration.Roomtypes
import com.google.android.material.bottomsheet.BottomSheetDialogFragment



class FlatBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentFlatBottomSheetBinding
    private lateinit var flat : Roomtypes

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFlatBottomSheetBinding.inflate(inflater, container, false)

        flat = arguments?.getSerializable("roomtypes") as Roomtypes
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            textFlatPrice.text = "Цена аренды: " + flat.price!!.toInt().toString() + "р"
            textFlatLocation.text = flat.address
            exampleOfFlatImageView.setImageResource(R.drawable.example_of_flat_bottom_fragment)
            countAdultsFlatTextView.text = "Количество взрослых: "+ flat.adults.toString()
            countChildrenFlatTextView.text = "Количество детей: " + flat.children.toString()

        }


    }

    override fun onStart() {
        super.onStart()


    }

}



