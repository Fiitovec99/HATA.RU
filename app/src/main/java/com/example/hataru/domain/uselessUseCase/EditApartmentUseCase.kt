package com.example.hataru.domain.uselessUseCase

import com.example.hataru.domain.uselessUseCase.Apartment
import com.example.hataru.domain.uselessUseCase.ApartmentListRepository

class EditApartmentUseCase(private val apartmentListRepository: ApartmentListRepository) {

    fun editApartment(apartment: Apartment) {
        apartmentListRepository.editApartment(apartment)
    }
}