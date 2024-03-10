package com.example.hataru.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.hataru.domain.entity.RoomtypeWithPhotos

class ApartmentDiffCallback: DiffUtil.ItemCallback<RoomtypeWithPhotos>() {

    override fun areItemsTheSame(oldItem: RoomtypeWithPhotos, newItem: RoomtypeWithPhotos): Boolean {
        return oldItem.roomtype.id == newItem.roomtype.id
    }

    override fun areContentsTheSame(oldItem: RoomtypeWithPhotos, newItem: RoomtypeWithPhotos): Boolean {
        return oldItem == newItem
    }
}