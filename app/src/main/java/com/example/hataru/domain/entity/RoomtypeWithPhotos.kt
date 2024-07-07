package com.example.hataru.domain.entity

data class RoomtypeWithPhotos(
    val roomtype: Roomtype,
    val photos: List<RoomX>
)
