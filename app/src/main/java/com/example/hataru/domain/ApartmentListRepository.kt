package com.example.hataru.domain

import androidx.lifecycle.LiveData

interface ApartmentListRepository {

    fun addApartment(apartment: Apartment)

    fun deleteApartment(apartment: Apartment)

    fun editApartment(apartment: Apartment)

    fun getApartmentList(): LiveData<List<Apartment>>

    fun getApartment(apartmentId: Int): Apartment
}