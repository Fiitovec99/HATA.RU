package com.example.hataru.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.hataru.domain.Apartment
import com.example.hataru.domain.entity.Roomtype

class ApartmentDiffCallback: DiffUtil.ItemCallback<Roomtype>() {

    override fun areItemsTheSame(oldItem: Roomtype, newItem: Roomtype): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Roomtype, newItem: Roomtype): Boolean {
        return oldItem == newItem
    }
}