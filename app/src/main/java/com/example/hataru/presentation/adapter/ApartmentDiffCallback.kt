package com.example.hataru.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.hataru.domain.Apartment

class ApartmentDiffCallback: DiffUtil.ItemCallback<Apartment>() {

    override fun areItemsTheSame(oldItem: Apartment, newItem: Apartment): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Apartment, newItem: Apartment): Boolean {
        return oldItem == newItem
    }
}