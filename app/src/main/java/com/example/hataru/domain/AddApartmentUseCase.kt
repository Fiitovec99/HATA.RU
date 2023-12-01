package com.example.hataru.domain

class AddApartmentUseCase(private val apartmentListRepository: ApartmentListRepository) {

    fun addApartment(apartment: Apartment) {
        apartmentListRepository.addApartment(apartment)
    }
}