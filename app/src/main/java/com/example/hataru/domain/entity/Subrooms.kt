package com.example.hataru.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Subrooms(

     var id: String? = null,
     var hotelId: String? = null,
     var parentId: String? = null,
     var name: String? = null,
     var type: String? = null,
     var adults: String? = null,
     var children: String? = null,
     var childrenAge: String? = null,
     var price: String? = null,
     var board: String? = null,
     var code: String? = null,
     var description: String? = null,
     var sortOrder: String? = null,
     var country: String? = null,
     var city: String? = null,
     var address: String? = null,
     var cityEng: String? = null,
     var addressEng: String? = null,
     var postcode: String? = null,
     var gisPoint: String? = null,
     var geoData: String? = null,
     var bookingUrl: String? = null,
     var tripadvisorUrl: String? = null,
     var providerRoomtypeSettings: ArrayList<String> = arrayListOf(),
     var providerRoomtypeId: String? = null,
     var deleted: String? = null,
     var extra: Extra? = Extra(),
     var subrooms: String? = null,
     var rooms: ArrayList<String> = arrayListOf()

) : Parcelable
