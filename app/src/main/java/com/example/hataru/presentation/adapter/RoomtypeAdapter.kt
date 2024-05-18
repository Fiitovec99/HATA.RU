package com.example.hataru.presentation.adapter

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.ListAdapter
import com.denzcoskun.imageslider.models.SlideModel
import com.example.hataru.R
import com.example.hataru.domain.entity.RoomtypeWithPhotos
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class RoomtypeAdapter(
    private val roomtypeWithPhotosList: List<RoomtypeWithPhotos>,
    private val context: Context
) : ListAdapter<RoomtypeWithPhotos, ApartmentViewHolder>(ApartmentDiffCallback()) {

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

    private var originalList: List<RoomtypeWithPhotos> = roomtypeWithPhotosList


    var onLikeButtonClickListener: ((RoomtypeWithPhotos) -> Unit)? = null
    var onApartmentClickListener: ((RoomtypeWithPhotos) -> Unit)? = null

    private val sharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    private val backgroundExecutor = Executors.newSingleThreadExecutor()
    private val uiScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApartmentViewHolder {
        // Определяем макет в зависимости от состояния избранного
        val layoutResId =
            if (viewType == VIEW_TYPE_LIKED) R.layout.apartment_liked else R.layout.apartment_noliked
        val view = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        return ApartmentViewHolder(view)
    }


    override fun onBindViewHolder(viewHolder: ApartmentViewHolder, position: Int) {
        val apartment = getItem(position)

        // инициализация полей
        val imageList = ArrayList<SlideModel>() // Create image list
        apartment.photos.forEach {
            imageList.add(SlideModel(it.url))
        }
        viewHolder.image_slider.setImageList(imageList)
        viewHolder.twShortDescription.text = mdesc[apartment.roomtype.id]
        viewHolder.twPrice.text = apartment.roomtype.price + "₽" // "Цена: " +
        viewHolder.twLevel.text = "Этаж: " + mlev[apartment.roomtype.id]
        viewHolder.twArea.text = marea[apartment.roomtype.id] + " кв.м."

        viewHolder.image_slider.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewHolder.image_slider.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val width = viewHolder.image_slider.width
                val height = (width / 1.5).toInt()
                viewHolder.image_slider.layoutParams.height = height
                viewHolder.image_slider.requestLayout()
            }
        })

        // Установка изображения кнопки лайка в зависимости от состояния избранного
        val isLiked = sharedPreferences.getBoolean(apartment.roomtype.name, false)
        val likeIconResId = if (isLiked) R.drawable.image_like else R.drawable.vector
        viewHolder.buttonLike.setImageResource(likeIconResId)

        // Обработчик нажатия на кнопку лайка
        viewHolder.buttonLike.setOnClickListener {
            // Обновление состояния избранного
            val newLikedState = !isLiked
            sharedPreferences.edit().putBoolean(apartment.roomtype.name, newLikedState).apply()
            // Обновление внешнего вида кнопки лайка
            val updatedLikeIconResId = if (newLikedState) R.drawable.image_like else R.drawable.vector
            viewHolder.buttonLike.setImageResource(updatedLikeIconResId)
            // Вызов обработчика нажатия, если он задан
            onLikeButtonClickListener?.invoke(apartment)
        }

        // Добавьте обработчик нажатия на элемент списка, если он задан
        viewHolder.itemView.setOnClickListener {
            onApartmentClickListener?.invoke(apartment)
        }
    }



    fun filter(query: String) {
        val filteredList = if (query.isEmpty() || query == "") {
            originalList
        } else {
            originalList.filter { roomtypeWithPhotos ->
                mdesc[roomtypeWithPhotos.roomtype.id]!!.contains(query, ignoreCase = true)
            }
        }
        submitList(filteredList)
    }

    override fun getItemViewType(position: Int): Int {
        val apartment = getItem(position)
        val isLiked = sharedPreferences.getBoolean(apartment.roomtype.name, false)
        return if (isLiked) VIEW_TYPE_LIKED else VIEW_TYPE_NOLIKED
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    companion object {
        const val VIEW_TYPE_LIKED = 1
        const val VIEW_TYPE_NOLIKED = 0
        const val MAX_POOL_SIZE = 7
    }

}
