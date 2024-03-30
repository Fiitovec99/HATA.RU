package com.example.hataru.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.models.SlideModel
import com.example.hataru.R
import com.example.hataru.domain.entity.Roomtype
import com.example.hataru.domain.entity.RoomtypeWithPhotos

class RoomtypeAdapter : ListAdapter<RoomtypeWithPhotos, ApartmentViewHolder>(ApartmentDiffCallback()) {


    var onLikeButtonClickListener: ((RoomtypeWithPhotos) -> Unit)? = null
    var onApartmentClickListener: ((RoomtypeWithPhotos) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApartmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.apartment_noliked, parent, false)
        return ApartmentViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ApartmentViewHolder, position: Int) {
        val apartment = getItem(position)

        val imageList = ArrayList<SlideModel>() // Create image list
        apartment.photos.forEach {
            imageList.add(SlideModel(it.url))

        }
        viewHolder.image_slider.setImageList(imageList)

        viewHolder.buttonLike.setOnClickListener {
            onLikeButtonClickListener?.invoke(apartment)
        }
        viewHolder.view.setOnClickListener {
            onApartmentClickListener?.invoke(apartment)
        }
        viewHolder.twShortDescription.text = "Однокомнатная квартира для 4-х гостей на " + apartment.roomtype.address
        viewHolder.twPrice.text = apartment.roomtype.price + "₽" // "Цена: " +




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
