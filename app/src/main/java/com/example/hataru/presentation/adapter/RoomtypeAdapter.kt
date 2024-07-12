package com.example.hataru.presentation.adapter

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
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



    private var originalList: List<RoomtypeWithPhotos> = roomtypeWithPhotosList


    var onLikeButtonClickListener: ((RoomtypeWithPhotos) -> Unit)? = null
    var onApartmentClickListener: ((RoomtypeWithPhotos) -> Unit)? = null

    private val sharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

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
        apartment.photos.forEach { roomX ->
            roomX.photos?.forEach {
                imageList.add(SlideModel(it.url))
            }
        }

        val description = if (apartment.photos.isNotEmpty()) {
            apartment.photos[0].name_ru ?: "Описание не доступно"
        } else {
            "Описание не доступно"
        }
        viewHolder.image_slider.setImageList(imageList)
        viewHolder.twShortDescription.text = extractDescription(description)
        viewHolder.twPrice.text = apartment.roomtype.price + "₽" // "Цена: " +
        viewHolder.twLevel.text = "Этаж: " + extractFloor(description)
        viewHolder.twArea.text = extractArea(description).toString() + " кв.м."



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
        val filteredList = if (query.isEmpty()) {
            originalList
        } else {
            originalList.filter { roomtypeWithPhotos ->
                roomtypeWithPhotos.roomtype.name!!.contains(query, ignoreCase = true)
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
