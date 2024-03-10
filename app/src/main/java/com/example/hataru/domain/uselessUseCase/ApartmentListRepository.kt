package com.example.hataru.domain.uselessUseCase

import androidx.lifecycle.LiveData
import com.example.hataru.domain.uselessUseCase.Apartment

interface ApartmentListRepository {

    fun addApartment(apartment: Apartment)

    fun deleteApartment(apartment: Apartment)

    fun editApartment(apartment: Apartment)

    fun getApartmentList(): LiveData<List<Apartment>>

    fun getApartment(apartmentId: Int): Apartment
}