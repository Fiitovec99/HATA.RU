package com.example.hataru.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.example.hataru.domain.uselessUseCase.Apartment
import com.example.hataru.domain.uselessUseCase.ApartmentListRepository

object ApartmentListRepositoryImpl: ApartmentListRepository {

    private val apartmentList = sortedSetOf<Apartment>({ o1, o2 -> o1.id.compareTo(o2.id)})
    private val apartmentListLD = MutableLiveData<List<Apartment>>()

    private var autoIncrementId = 0

//    init{
//        for (ap in roomTypes){
//            val item = Apartment("Адрес: " + ap.address!!, ap.description!!, ap.adults!!,
//                ap.price.toInt()!!,(Random.nextDouble(10.0,30.0)*10).roundToInt()/10.0, false, ap.id!!.toInt()
//            )
//            addApartment(item)
//        }
//    }

    override fun addApartment(apartment: Apartment) {
        if (apartment.id == Apartment.UNDEFIND_ID){
            apartment.id = autoIncrementId++
        }
        apartmentList.add(apartment)
        updateList()
    }

    override fun deleteApartment(apartment: Apartment) {
        apartmentList.remove(apartment)
        updateList()
    }

    override fun editApartment(apartment: Apartment) {
        val oldElement = getApartment(apartment.id)
        apartmentList.remove(oldElement)
        addApartment(apartment)
    }

    override fun getApartmentList(): LiveData<List<Apartment>> {
        return apartmentListLD
    }

    override fun getApartment(apartmentId: Int): Apartment {
        return apartmentList.find {
            it.id == apartmentId
        } ?: throw RuntimeException("Element with id $apartmentId not found")
    }

    private fun updateList() {
        apartmentListLD.value = apartmentList.toList()
    }
}