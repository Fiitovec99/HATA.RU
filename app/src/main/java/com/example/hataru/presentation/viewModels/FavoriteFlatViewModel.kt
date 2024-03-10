package com.example.hataru.presentation.viewModels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.hataru.data.ApartmentListRepositoryImpl
import com.example.hataru.domain.uselessUseCase.Apartment
import com.example.hataru.domain.uselessUseCase.EditApartmentUseCase
import com.example.hataru.domain.uselessUseCase.GetApartmentListUseCase

class FavoriteFlatViewModel : ViewModel() {

    private val repository = ApartmentListRepositoryImpl

    private val getApartmentListUseCase = GetApartmentListUseCase(repository)
    private val editApartmentUseCase = EditApartmentUseCase(repository)

    private val originalApartmentList = getApartmentListUseCase.getApartmentList()

    val apartmentList = MediatorLiveData<List<Apartment>>()

    init {
        apartmentList.addSource(originalApartmentList) { originalApartmentList ->
            apartmentList.value = originalApartmentList.filter { it.liked == true }
        }
    }

    fun changeLikedStage(apartment: Apartment) {
        val newItem = apartment.copy(liked = !apartment.liked)
        editApartmentUseCase.editApartment(newItem)
    }
}