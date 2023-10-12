package com.example.hataru.presentation.forMap

enum class PlacemarkType {
    YELLOW,
    GREEN,
    RED
}

data class PlacemarkUserData(
    val name: String,
    val type: PlacemarkType,
)