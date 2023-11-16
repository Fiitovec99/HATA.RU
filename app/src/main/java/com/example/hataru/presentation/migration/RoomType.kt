package com.example.hataru.presentation.migration




data class RoomType(
        val id: String,
        val hotel_id: String,
        val parent_id: String,
        val name: String,
        val type: String,
        val adults: String,
        val children: String,
        val price: String,
        val board: String,
        val description: String,
        val sort_order: String?, // Могут быть null, поэтому используем nullable тип
        val country: String?,
        val city: String?,
        val city_eng: String?,
        val address: String?,
        val address_eng: String?,
        val postcode: String?,
        val geo_data: GeoData?,
        val subrooms: List<Any>, // Ваша схема указывает на список, но содержание не описано
        val rooms: List<Room>
)

data class GeoData(
        val x: String,
        val y: String
)

data class AuthenticationResponse(
        val token: String,
        // Другие поля, если есть
)



data class AuthRequest(val username: String, val password: String)


data class UserCredentials(
        val username: String,
        val password: String
)


data class Room(
        val id: String,
        val hotel_id: String,
        val room_type_id: String,
        val room_type_name: String?,
        val name: String,
        val tags: String,
        val sort_order: String
)

