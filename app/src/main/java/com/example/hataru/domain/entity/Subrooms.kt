package com.example.hataru.domain.entity

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.parcelize.Parcelize

@Parcelize
data class Subrooms(

    @JsonProperty("id") var id: String? = null,
    @JsonProperty("hotel_id") var hotelId: String? = null,
    @JsonProperty("parent_id") var parentId: String? = null,
    @JsonProperty("name") var name: String? = null,
    @JsonProperty("type") var type: String? = null,
    @JsonProperty("adults") var adults: String? = null,
    @JsonProperty("children") var children: String? = null,
    @JsonProperty("children_age") var childrenAge: String? = null,
    @JsonProperty("price") var price: String? = null,
    @JsonProperty("board") var board: String? = null,
    @JsonProperty("code") var code: String? = null,
    @JsonProperty("description") var description: String? = null,
    @JsonProperty("sort_order") var sortOrder: String? = null,
    @JsonProperty("country") var country: String? = null,
    @JsonProperty("city") var city: String? = null,
    @JsonProperty("address") var address: String? = null,
    @JsonProperty("city_eng") var cityEng: String? = null,
    @JsonProperty("address_eng") var addressEng: String? = null,
    @JsonProperty("postcode") var postcode: String? = null,
    @JsonProperty("gis_point") var gisPoint: String? = null,
    @JsonProperty("geo_data") var geoData: String? = null,
    @JsonProperty("booking_url") var bookingUrl: String? = null,
    @JsonProperty("tripadvisor_url") var tripadvisorUrl: String? = null,
    @JsonProperty("provider_roomtype_settings") var providerRoomtypeSettings: ArrayList<String> = arrayListOf(),
    @JsonProperty("provider_roomtype_id") var providerRoomtypeId: String? = null,
    @JsonProperty("deleted") var deleted: String? = null,
    @JsonProperty("extra") var extra: Extra? = Extra(),
    @JsonProperty("subrooms") var subrooms: String? = null,
    @JsonProperty("rooms") var rooms: ArrayList<String> = arrayListOf()

) : Parcelable
