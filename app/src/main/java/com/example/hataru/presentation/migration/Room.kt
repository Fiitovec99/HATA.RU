package com.example.hataru.presentation.migration

data class Room(
    val id: String,
    val hotel_id: String,
    val room_type_id: String,
    val room_type_name: String?,
    val name: String,
    val tags: String,
    val sort_order: String,
    val clean_status: String?,
    val room_type: String
)
