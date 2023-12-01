package com.example.hataru.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hataru.R
import com.example.hataru.databinding.FragmentFlatBottomSheetBinding
import com.example.hataru.domain.entity.Roomtypes
import com.example.hataru.presentation.SpaceItemDecoration
import com.example.hataru.presentation.adapter.PhotoAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.Serializable


class FlatBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentFlatBottomSheetBinding
    private lateinit var flat : Roomtypes

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFlatBottomSheetBinding.inflate(inflater, container, false)

        flat =  arguments?.getSerializable("roomtypes") as Roomtypes
        val photoRecyclerView: RecyclerView = binding.photoRecyclerView

        // Получите массив ресурсов изображений
        val photoResources = resources.obtainTypedArray(R.array.photo_resources)

        val photos = mutableListOf<Int>()
        // Добавьте первые 4 изображения из массива ресурсов в список
        for (i in 0 ..3) {
            photos.add(photoResources.getResourceId(i, 0))
        }

        photoResources.recycle()

        val photoAdapter = PhotoAdapter(photos)
        photoRecyclerView.adapter = photoAdapter

        // Настройте LayoutManager для RecyclerView (например, LinearLayoutManager)
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val spaceInPixels = resources.getDimensionPixelSize(R.dimen.space_between_photos)
        photoRecyclerView.addItemDecoration(SpaceItemDecoration(spaceInPixels))
        photoRecyclerView.layoutManager = layoutManager


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            textFlatPrice.text = "Цена аренды: " + flat.price!!.toInt().toString() + "р"
            textFlatLocation.text = flat.address
            countAdultsFlatTextView.text = "Количество взрослых: "+ flat.adults.toString()
            countChildrenFlatTextView.text = "Количество детей: " + flat.children.toString()

            buttonOpeningFlat.setOnClickListener{

                val args = Bundle()
                args.putSerializable("roomtypes", flat as Serializable)

                findNavController().navigate(R.id.flatFragment,args)
            }
        }


    }



}



