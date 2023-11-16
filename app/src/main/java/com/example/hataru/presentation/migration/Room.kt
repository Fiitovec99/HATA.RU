//package com.example.hataru.presentation.migration
//
//import com.google.gson.annotations.SerializedName
//
//data class Room1(
//    @SerializedName("id") private val _id: String?,
//    @SerializedName("hotel_id") private val _hotel_id: String?,
//    @SerializedName("room_type_id") private val _room_type_id: String?,
//    @SerializedName("room_type_name") private val _room_type_name: String?,
//    @SerializedName("name") private val _name: String?,
//    @SerializedName("tags") private val _tags: String?,
//    @SerializedName("sort_order") private val _sort_order: String?,
//    @SerializedName("clean_status") private val _clean_status: String?,
//    @SerializedName("room_type") private val _room_type: String?
//){
//    val id
//        get() = _id ?: "id отсутствует"
//
//    val hotel_id
//        get() = _hotel_id ?: "hotel_id отсутствует"
//
//    val room_type_id
//        get() = _room_type_id ?: "room_type_id отсутствует"
//
//    val room_type_name
//        get() = _room_type_name ?: "room_type_name отсутствует"
//
//    val name
//        get() = _name ?: "name отсутствует"
//
//    val tags
//        get() = _tags ?: "tags отсутствует"
//
//    val sort_order
//        get() = _sort_order ?: "sort_order отсутствует"
//
//    val clean_status
//        get() = _clean_status ?: "clean_status отсутствует"
//
//    val room_type
//        get() = _room_type ?: "room_type отсутствует"
//
//
//    init {
//        this.id
//        this.hotel_id
//        this.room_type_id
//        this.room_type_name
//        this.name
//        this.tags
//        this.sort_order
//        this.clean_status
//        this.room_type
//    }
//}
