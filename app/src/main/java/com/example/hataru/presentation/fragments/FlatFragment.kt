package com.example.hataru.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.denzcoskun.imageslider.models.SlideModel
import com.example.hataru.R
import com.example.hataru.databinding.FragmentFlatBinding
import com.example.hataru.domain.entity.Photo
import com.example.hataru.domain.entity.Photos
import com.example.hataru.domain.entity.Roomtype
import com.example.hataru.domain.entity.RoomtypeWithPhotos
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
            textShortDescription.text = "Однокомнатная квартира для 4-х гостей на " + flat.address
            textFlatPrice.text = flat.price!!.toDouble().toString() + "₽"
//            textFlatLocation.text = flat.address
            Equipment.text = "Комфортабельная кровать 160х200 см\n" +
                    "Чистая сатиновое белье из прачечной\n" +
                    "Раскладывающийся диван\n" +
                    "Шампунь, гель для душа, полотенца\n" +
                    "Телевизор с выходом в интернет\n" +
                    "Посуда и столовые принадлежности\n" +
                    "Электрический чайник, микроволновая печь, гладильная доска, утюг, фен, сушилка для одежды\n" +
                    "Современная сплит-система\n" +
                    "Стиральная машина"
            Adress.text = "Адрес: " + flat.address
            description.text = flat.description
            Location.text = "Рядом: Удобный выезд на трассу М-4 Дон, магазины, супермаркет \"Магнит\", аптека, остановки общественного транспорта. ТЦ \"Мега\"."
//            countAdultsFlatTextView.text = "Количество взрослых: "+ flat.adults.toString()
//            countChildrenFlatTextView.text = "Количество детей: " + flat.children.toString()

            buttonLike.setOnClickListener {

            }


        }

        view.findViewById<Button>(R.id.button2)?.setOnClickListener {
            showToast("В разработке!")
        }

        view.findViewById<Button>(R.id.button3)?.setOnClickListener {
            showToast("В разработке!")
        }

    }



    override fun onStart() {
        val imageList = ArrayList<SlideModel>() // Create image list
        var photosList = mutableListOf<Photo>()
        viewModel.photo.observe(viewLifecycleOwner){

            it.rooms.forEach {
                if(it.name == flat.name.toString()){

                    it.photos?.forEach {
                            photo ->
                        photo.url?.let { it1 -> imageList.add(SlideModel(photo.url))}
                        binding.imageSlider.setImageList(imageList)
                        photosList.add(photo)
                    }
                }
            }
        }

        binding.buttonLike.setOnClickListener {
            viewModel.changeLikedStage(RoomtypeWithPhotos(roomtype = flat, photos = photosList))
            showToast("Квартира добавлена в избранные!")

        }

        super.onStart()
    }

    companion object{

        const val KEY_GET_FLAT_INTO_FLATFRAGMENT = "KEY_GET_FLAT"
    }



}