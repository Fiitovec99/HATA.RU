package com.example.hataru.domain.uselessUseCase

import androidx.lifecycle.LiveData
import com.example.hataru.domain.uselessUseCase.Apartment
import com.example.hataru.domain.uselessUseCase.ApartmentListRepository

class GetApartmentListUseCase(private val apartmentListRepository: ApartmentListRepository) {

    fun getApartmentList(): LiveData<List<Apartment>> {
        return apartmentListRepository.getApartmentList()
    }
}