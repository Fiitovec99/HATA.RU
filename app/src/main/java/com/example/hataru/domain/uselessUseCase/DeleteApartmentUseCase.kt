package com.example.hataru.domain.uselessUseCase

import com.example.hataru.domain.uselessUseCase.Apartment
import com.example.hataru.domain.uselessUseCase.ApartmentListRepository

class DeleteApartmentUseCase(private val apartmentListRepository: ApartmentListRepository) {

    fun deleteApartment(apartment: Apartment) {
        apartmentListRepository.deleteApartment(apartment)
    }
}