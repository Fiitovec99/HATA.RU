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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.yandex.mapkit.directions.driving.Description
import org.koin.androidx.viewmodel.ext.android.viewModel

class FlatFragment : Fragment() {
    
    fun extractFloor(log: String?): Int? {
        val floorRegex = """Этаж:\s*\"(\d+)\"""".toRegex()
        val matchResult = log?.let { floorRegex.find(it) }
        return matchResult?.groups?.get(1)?.value?.toInt()
    }



    fun extractArea(log: String): Int? {
        try {
            val areaRegex = """Площадь:\s*\"(\d+)\"""".toRegex()
            val matchResult = log.let { areaRegex.find(it) }
            return matchResult!!.groups!!.get(1)?.value?.toInt()
        }catch (e : NullPointerException){
            return 100000000
        }
    }


    fun extractDescription(log: String): String? {
        try{
            val descriptionRegex = """Описание:\s*\"([^\"]+)\"""".toRegex()
            val matchResult = log.let { descriptionRegex.find(it) }
            return matchResult!!.groups.get(1)?.value
        }catch (e : NullPointerException){
            return "не приходят данные"
        }

    }

    private val viewModel by viewModel<FlatViewModel>()
    private lateinit var binding : FragmentFlatBinding
    private lateinit var flat : Roomtype

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentFlatBinding.inflate(layoutInflater,container,false)

        flat = arguments?.getParcelable(KEY_GET_FLAT_INTO_FLATFRAGMENT) as? Roomtype
            ?: throw IllegalArgumentException("Argument is null or has unexpected type")

        Log.d("Flat",flat.toString())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val descriptionfrombnovo = arguments?.getString("description").toString()
        Log.d("Description", descriptionfrombnovo ?: "Description is null")
        binding.apply {
            textShortDescription.text = extractDescription(descriptionfrombnovo)
            Log.d("Description", description.toString() ?: "Description is null")
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
            textViewArea.text = extractArea(descriptionfrombnovo).toString() + " кв.м."
            textViewLevel.text = "Этаж: " + extractFloor(descriptionfrombnovo)

            ////////////////////////////////////////////////

//            val userId = FirebaseAuth.getInstance().currentUser?.uid
//            if (userId != null && flat.id != null) {
//                val favoriteFlatDocument = FirebaseFirestore.getInstance().collection(userId).document(flat.id!!)
//                favoriteFlatDocument.get()
//                    .addOnSuccessListener { document ->
//                        if (document.exists()) {
//                            // Квартира уже в избранных
//                            buttonLike.setBackgroundResource(R.drawable.image_like)
//                        } else {
//                            // Квартиры нет в избранных
//                            buttonLike.setBackgroundResource(R.drawable.vector)
//                        }
//                    }
//                    .addOnFailureListener { e ->
//                        Log.w("TAG", "Error checking if room is in favorites", e)
//                    }
//            }


        }

        view.findViewById<Button>(R.id.button2)?.setOnClickListener {
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

//        binding.apply {
//            buttonLike.setOnClickListener {
//                val userId = FirebaseAuth.getInstance().currentUser?.uid
//                if (userId != null && flat.id != null) {
//                    val favoriteFlatDocument = FirebaseFirestore.getInstance().collection(userId).document(flat.id!!)
//                    favoriteFlatDocument.get()
//                        .addOnSuccessListener { document ->
//                            if (document.exists()) {
//                                // Квартира уже в избранных, убираем ее
//                                favoriteFlatDocument.delete()
//                                    .addOnSuccessListener {
//                                        Log.d("TAG", "Room removed from favorites: ${flat.id}")
//                                        buttonLike.setBackgroundResource(R.drawable.vector)
//                                    }
//                                    .addOnFailureListener { e ->
//                                        Log.w("TAG", "Error removing room from favorites", e)
//                                    }
//                            } else {
//                                // Квартиры нет в избранном, добавляем ее
//                                favoriteFlatDocument.set(RoomtypeWithPhotos(flat,photosList))
//                                    .addOnSuccessListener {
//                                        Log.d("TAG", "Room added to favorites: ${flat.id}")
//                                       buttonLike.setBackgroundResource(R.drawable.image_like)
//                                    }
//                                    .addOnFailureListener { e ->
//                                        Log.w("TAG", "Error adding room to favorites", e)
//                                    }
//                            }
//                        }
//                        .addOnFailureListener { e ->
//                            Log.w("TAG", "Error checking if room is in favorites", e)
//                        }
//                }
//            }
//        }



        super.onStart()
    }

    companion object{

        const val KEY_GET_FLAT_INTO_FLATFRAGMENT = "KEY_GET_FLAT"
    }



}