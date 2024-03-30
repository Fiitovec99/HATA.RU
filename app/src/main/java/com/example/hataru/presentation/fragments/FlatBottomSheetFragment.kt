package com.example.hataru.presentation.fragments

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.models.SlideModel
import com.example.hataru.R
import com.example.hataru.databinding.FragmentFlatBottomSheetBinding
import com.example.hataru.domain.entity.Roomtype
import com.example.hataru.presentation.adapter.PhotoAdapter
import com.example.hataru.presentation.fragments.FlatFragment.Companion.KEY_GET_FLAT_INTO_FLATFRAGMENT
import com.example.hataru.presentation.viewModels.FlatBottomSheetViewModel
import com.example.hataru.presentation.viewModels.MapViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.Serializable


class FlatBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentFlatBottomSheetBinding
    private lateinit var flat : Roomtype
    private val viewModel by viewModel<FlatBottomSheetViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFlatBottomSheetBinding.inflate(inflater, container, false)

        flat = (arguments?.getParcelable(KEY_GET_FLAT) as? Roomtype)!!








        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.apply {
            textViewPrice.text = flat.price + "₽"
            textShortDescription.text = "Однокомнатная квартира для 4-х гостей на " + flat.address

            buttonOpeningFlat.setOnClickListener{

                val args = Bundle()
                args.putParcelable(KEY_GET_FLAT_INTO_FLATFRAGMENT, flat as Parcelable)

                findNavController().navigate(R.id.flatFragment,args)
            }
        }


    }

    override fun onStart() {
        val imageList = ArrayList<SlideModel>() // Create image list
        viewModel.url.observe(viewLifecycleOwner){
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
        const val KEY_GET_FLAT = "GET_FLAT"

    }



}



