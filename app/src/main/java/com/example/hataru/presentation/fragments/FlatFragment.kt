package com.example.hataru.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.example.hataru.R
import com.example.hataru.databinding.FragmentFlatBinding
import com.example.hataru.domain.entity.RoomX
import com.example.hataru.domain.entity.Roomtype
import com.example.hataru.domain.entity.Roomtypes
import com.example.hataru.presentation.fragments.FlatBottomSheetFragment.Companion.KEY_GET_FLAT
import com.example.hataru.presentation.viewModels.FlatViewModel
import com.example.hataru.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel

class FlatFragment : Fragment() {

    private val viewModel by viewModel<FlatViewModel>()
    private lateinit var binding : FragmentFlatBinding
    private lateinit var flat : Roomtype

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFlatBinding.inflate(layoutInflater,container,false)

        flat = arguments?.getParcelable(KEY_GET_FLAT_INTO_FLATFRAGMENT) as? Roomtype
            ?: throw IllegalArgumentException("Argument is null or has unexpected type")

//        Toast.makeText(requireContext(),flat.toString(),Toast.LENGTH_LONG).show()
        //Log.d("asad",flat.toString())
        Log.d("Flat",flat.toString())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            textFlatPrice.text = "Цена аренды: " + flat.price!!.toDouble().toString() + "р"
            textFlatLocation.text = flat.address
            countAdultsFlatTextView.text = "Количество взрослых: "+ flat.adults.toString()
            countChildrenFlatTextView.text = "Количество детей: " + flat.children.toString()
            description.text = flat.description


        }


        //Log.d("flat",flat.toString())

        //Log.d("asdasd", photos?.filter { roomX: RoomX -> roomX.name.trim() == flat.name.toString().trim() }.toString())





//        imageList.add(SlideModel("https://bit.ly/2YoJ77H", "The animal population decreased by 58 percent in 42 years."))
//        imageList.add(SlideModel("https://bit.ly/2BteuF2", "Elephants and tigers may become extinct."))
//        imageList.add(SlideModel("https://bit.ly/3fLJf72", "And people do that."))





    }

    override fun onStart() {
        val imageList = ArrayList<SlideModel>() // Create image list
        viewModel.photo.observe(viewLifecycleOwner){
            it.rooms.forEach {
                if(it.name == flat.name.toString()){
                    it.photos?.forEach {
                        photo ->
                        photo.url?.let { it1 -> imageList.add(SlideModel(photo.url))}
                        binding.imageSlider.setImageList(imageList)
                    }
                }
            }
        }

        super.onStart()
    }

    companion object{

        const val KEY_GET_FLAT_INTO_FLATFRAGMENT = "KEY_GET_FLAT"
    }



}