package hataru.presentation

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import hataru.data.ApartmentListRepositoryImpl
import hataru.domain.Apartment
import hataru.domain.EditApartmentUseCase
import hataru.domain.GetApartmentListUseCase

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