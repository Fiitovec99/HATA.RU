package com.example.listrooms.presentation

import androidx.lifecycle.ViewModel
import hataru.data.ApartmentListRepositoryImpl
import hataru.domain.Apartment
import hataru.domain.EditApartmentUseCase
import hataru.domain.GetApartmentListUseCase

class ListFlatsViewModel : ViewModel() {

    private val repository = ApartmentListRepositoryImpl

    private val getApartmentListUseCase = GetApartmentListUseCase(repository)
    private val editApartmentUseCase = EditApartmentUseCase(repository)

    val apartmentList = getApartmentListUseCase.getApartmentList()

    fun changeLikedStage(apartment: Apartment) {
        val newItem = apartment.copy(liked = !apartment.liked)
        editApartmentUseCase.editApartment(newItem)
    }
}