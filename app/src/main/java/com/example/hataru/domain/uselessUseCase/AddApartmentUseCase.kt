package com.example.hataru.domain.uselessUseCase

class AddApartmentUseCase(private val apartmentListRepository: ApartmentListRepository) {

    fun addApartment(apartment: Apartment) {
        apartmentListRepository.addApartment(apartment)
    }
}