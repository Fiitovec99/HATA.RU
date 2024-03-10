package com.example.hataru.domain.uselessUseCase

import com.example.hataru.domain.uselessUseCase.Apartment
import com.example.hataru.domain.uselessUseCase.ApartmentListRepository

class GetApartmentUseCase(private val apartmentListRepository: ApartmentListRepository) {

    fun getApartment(apartmentId: Int): Apartment {
        return apartmentListRepository.getApartment(apartmentId)
    }
}