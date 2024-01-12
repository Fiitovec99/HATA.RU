package com.example.hataru.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize



@Parcelize
data class Room (
    var id: String? = null,
    var hotel_id: String? = null,
    var room_type_id: String? = null,
    var room_type_name: String? = null,
    var name: String? = null,
    var tags: String? = null,
    var sort_order: String? = null,
) : Parcelable