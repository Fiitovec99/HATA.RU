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

    var mdesc = mapOf<String, String>(
        "467150" to "Однокомнатная квартира на Сиверса 32 для 4 человек",
        "394665" to "Однокомнатная квартира для 4х гостей на Гвардейском 13",
        "389394" to "Однокомнатная квартира для 4х гостей на Шолохова 211 корпус 3",
        "354060" to "Однокомнатная квартира для 4х гостей на пр. Шолохова д. 211/2",
        "396679" to "Однокомнатная квартира для 4х гостей на Мечникова 37",
        "398045" to "Однокомнатная квартира для 4х гостей на Мечникова 37",
        "460294" to "Уютная студия для 4х гостей на Мечникова 37",
        "429750" to "Студия для 3х гостей на Нансена 83",
        "221997" to "Однокомнатная квартира для 4х гостей в доме бизнес класса на ул. Тельмана д. 110с2",
        "455567" to "Двухкомнатная квартира для 6 гостей на Соколова 68",
        "366723" to "Однокомнатная квартира для 4х гостей на ул. Береговая д.6",
        "478096" to "Однокомнатная квартира для 4х гостей на Ларина 45",
        "236104" to "Двухкомнатная квартира для 6х гостей на пр. Михаила Нагибина д.14Г",
        "279779" to "Однокомнатная квартира для 4х гостей на ул. Магнитогорская д.2Б",
        "480849" to "Однокомнатная квартира для 4х гостей на Магнитогорской 2",
        "325568" to "Однокомнатная квартира для 4х гостей на пер. Соборный д. 98",
        "221995" to "Однокомнатная квартира для 4х гостей на пер. Доломановский д.124с2",
        "221996" to "Однокомнатная квартира для 4х гостей на пер. Доломановский д.124с2",
        "222541" to "Двухкомнатная квартира для 6х гостей на ул. Черепахина д.212",
        "222542" to "Двухкомнатная квартира для 4х гостей на ул. Черепахина д.212",
        "222543" to "Двухкомнатная квартира для 4х гостей на ул. Черепахина д.212",
        "349715" to "Двухкомнатная квартира для 4х гостей на ул. Черепахина д. 212"
    )

    var mlev = mapOf<String, String>(
        "467150" to "13",
        "394665" to "8",
        "389394" to "4",
        "354060" to "3",
        "396679" to "17",
        "398045" to "13",
        "460294" to "7",
        "429750" to "17",
        "221997" to "17",
        "455567" to "5",
        "366723" to "5",
        "478096" to "8",
        "236104" to "14",
        "279779" to "8",
        "480849" to "5",
        "325568" to "21",
        "221995" to "14",
        "221996" to "18",
        "222541" to "9",
        "222542" to "8",
        "222543" to "7",
        "349715" to "6"
    )

    var marea = mapOf<String, String>(
        "467150" to "33",
        "394665" to "44",
        "389394" to "40",
        "354060" to "42",
        "396679" to "30",
        "398045" to "30",
        "460294" to "25",
        "429750" to "20",
        "221997" to "47",
        "455567" to "65",
        "366723" to "27",
        "478096" to "42",
        "236104" to "65",
        "279779" to "40",
        "480849" to "39",
        "325568" to "42",
        "221995" to "41",
        "221996" to "42",
        "222541" to "58",
        "222542" to "58",
        "222543" to "58",
        "349715" to "55"
    )

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
            textShortDescription.text = mdesc[flat.id]
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
            textViewArea.text = marea[flat.id] + " кв.м."
            textViewLevel.text = "Этаж: " + mlev[flat.id]

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