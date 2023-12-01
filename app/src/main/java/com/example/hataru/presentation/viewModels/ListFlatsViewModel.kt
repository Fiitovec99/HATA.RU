package com.example.hataru.presentation.viewModels

import androidx.lifecycle.ViewModel
import com.example.hataru.data.ApartmentListRepositoryImpl
import com.example.hataru.domain.Apartment
import com.example.hataru.domain.EditApartmentUseCase
import com.example.hataru.domain.GetApartmentListUseCase

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