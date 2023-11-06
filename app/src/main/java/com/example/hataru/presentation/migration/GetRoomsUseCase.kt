package com.example.hataru.presentation.migration

class GetRoomsUseCase(private val hotelRepository: HotelRepository) {
    fun execute(): List<Room>? {
        val roomResponse = hotelRepository.getRooms()
        return roomResponse?.rooms
    }
}