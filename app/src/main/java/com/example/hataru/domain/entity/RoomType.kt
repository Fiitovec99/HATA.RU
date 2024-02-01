package com.example.hataru.domain.entity

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.parcelize.Parcelize





@Parcelize
data class Roomtype (
    var id: String? = null,
    var hotel_id: String? = null,
    var parent_id: String? = null,
    var name: String? = null,
    var type: String? = null,
    var adults: String? = null,
    var children: String? = null,
    var price: String,
    var board: String? = null,
    var description: String? = null,
    var sort_order: String? = null,
    var country: String? = null,
    var city: String? = null,
    var city_eng: String? = null,
    var address: String? = null,
    var address_eng: String? = null,
    var postcode: String? = null,
    var geo_data: GeoData? = null,
    var subrooms: ArrayList<Subroom>? = null,
    var rooms: ArrayList<Room>? = null,
) : Parcelable





