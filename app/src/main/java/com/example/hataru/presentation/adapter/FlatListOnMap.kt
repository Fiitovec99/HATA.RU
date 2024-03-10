package com.example.hataru.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hataru.R
import com.example.hataru.domain.entity.Roomtype


class FlatListOnMap(private var flatList: List<Roomtype>) :
    RecyclerView.Adapter<FlatListOnMap.FlatViewHolder>() {

    inner class FlatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewPrice: TextView = itemView.findViewById(R.id.text_View_Price) // Replace with your actual TextView ID
        // Add other TextViews or views as needed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlatViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.apartment_noliked, parent, false)
        return FlatViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FlatViewHolder, position: Int) {
        val currentFlat = flatList[position]

        // Populate the TextView (and other views) with the data from currentFlat
        holder.textViewPrice.text = currentFlat.price.toString() // Adjust accordingly
        // Populate other views as needed
    }

    override fun getItemCount(): Int {
        return flatList.size
    }

    // Method to update the dataset
    fun updateFlats(newFlatList: List<Roomtype>) {
        flatList = newFlatList
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }
}
