import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.denzcoskun.imageslider.models.SlideModel
import com.example.hataru.R
import com.example.hataru.databinding.FragmentFlatFragmentViewPagerBinding
import com.example.hataru.domain.entity.Photo
import com.example.hataru.domain.entity.Roomtype
import com.example.hataru.domain.entity.RoomtypeWithPhotos
import com.example.hataru.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.androidx.viewmodel.ext.android.viewModel

class FlatFragmentViewPager : Fragment() {

    private val viewModel by viewModel<FlatFragmentViewPagerViewModel>()
    private lateinit var binding: FragmentFlatFragmentViewPagerBinding
    private lateinit var flat: Roomtype
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentFlatFragmentViewPagerBinding.inflate(layoutInflater, container, false)
        viewPager = requireActivity().findViewById(R.id.viewPager)

        flat = arguments?.getParcelable(KEY_GET_FLAT_INTO_FLATFRAGMENT) as? Roomtype
            ?: throw IllegalArgumentException("Argument is null or has unexpected type")

        Log.d("Flat", flat.toString())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

//        val userId = FirebaseAuth.getInstance().currentUser?.uid
//        if (userId != null && flat.id != null) {
//            val favoriteFlatDocument = FirebaseFirestore.getInstance().collection(userId).document(flat.id!!)
//            favoriteFlatDocument.get()
//                .addOnSuccessListener { document ->
//                    if (document.exists()) {
//                        // Квартира уже в избранных
//                        binding.buttonLike.setBackgroundResource(R.drawable.image_like)
//                    } else {
//                        // Квартиры нет в избранных
//                        binding.buttonLike.setBackgroundResource(R.drawable.vector)
//                    }
//                }
//                .addOnFailureListener { e ->
//                    Log.w("TAG", "Error checking if room is in favorites", e)
//                }
//        }
//
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
//                                        buttonLike.setBackgroundResource(R.drawable.image_like)
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

        binding.apply {
            textShortDescription.text = "Однокомнатная квартира для 4-х гостей на " + flat.address
            textFlatPrice.text = flat.price!!.toDouble().toString() + "₽"
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
        }





        binding.nestedScrollView.setOnTouchListener { _, event ->
            // Получаем координаты касания
            val x = event.x
            val y = event.y

            // Проверяем, находится ли касание в области imageSlider
            val isInImageSliderArea = isPointInsideView(x, y, binding.imageSlider)

            // Разрешаем или запрещаем прокрутку ViewPager2 в зависимости от результата
            viewPager.isUserInputEnabled = !isInImageSliderArea

            // Возвращаем false, чтобы обеспечить нормальную обработку касаний
            false
        }




    }

    companion object {
        const val KEY_GET_FLAT_INTO_FLATFRAGMENT = "KEY_GET_FLAT"
    }


    private fun isPointInsideView(x: Float, y: Float, view: View): Boolean {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val viewX = location[0]
        val viewY = location[1]

        // Получаем координаты границ области view
        val left = viewX
        val right = left + view.width
        val top = viewY
        val bottom = top + view.height

        // Проверяем, находится ли точка (x, y) внутри области view
        return x >= left && x <= right && y >= top && y <= bottom
    }

}






