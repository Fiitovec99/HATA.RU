package com.example.listrooms.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.hataru.R
import hataru.domain.Apartment

class ApartmentListAdapter : ListAdapter<Apartment, ApartmentViewHolder>(ApartmentDiffCallback()) {

    var onLikeButtonClickListener: ((Apartment) -> Unit)? = null
    var onApartmentClickListener: ((Apartment) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApartmentViewHolder {
        val layout = when (viewType) {
            VIEW_TYPE_NOLIKED -> R.layout.apartment_noliked
            VIEW_TYPE_LIKED -> R.layout.apartment_liked
            else -> throw RuntimeException("Unknown view type: $viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ApartmentViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ApartmentViewHolder, position: Int) {
        val apartment = getItem(position)
        viewHolder.buttonLike.setOnClickListener {
            onLikeButtonClickListener?.invoke(apartment)
        }
        viewHolder.view.setOnClickListener {
            onApartmentClickListener?.invoke(apartment)
        }
        viewHolder.twAddress.text = apartment.address
        viewHolder.twArea.text = apartment.area.toString()
        viewHolder.twGuests.text = apartment.people.toString()
        viewHolder.twPrice.text = apartment.price.toString()
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.liked) {
            VIEW_TYPE_LIKED
        } else {
            VIEW_TYPE_NOLIKED
        }
    }

    companion object {

        const val VIEW_TYPE_LIKED = 1
        const val VIEW_TYPE_NOLIKED = 0

        const val MAX_POOL_SIZE = 7
    }

}