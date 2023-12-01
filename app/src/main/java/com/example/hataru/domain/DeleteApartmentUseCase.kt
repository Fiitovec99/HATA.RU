package com.example.hataru.domain

class DeleteApartmentUseCase(private val apartmentListRepository: ApartmentListRepository) {

    fun deleteApartment(apartment: Apartment) {
        apartmentListRepository.deleteApartment(apartment)
    }
}