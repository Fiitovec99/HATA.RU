package hataru.domain

import androidx.lifecycle.LiveData

class GetApartmentListUseCase(private val apartmentListRepository: ApartmentListRepository) {

    fun getApartmentList(): LiveData<List<Apartment>> {
        return apartmentListRepository.getApartmentList()
    }
}