package hataru.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hataru.data.ApartmentListRepositoryImpl
import hataru.domain.Apartment
import hataru.domain.EditApartmentUseCase
import hataru.domain.GetApartmentUseCase

class ApartmentViewModel : ViewModel() {

    private val repository = ApartmentListRepositoryImpl

    private val getApartmentUseCase = GetApartmentUseCase(repository)
    private val editApartmentUseCase = EditApartmentUseCase(repository)

    private val _apartment = MutableLiveData<Apartment>()
    val apartment: LiveData<Apartment>
        get() = _apartment

    fun getApartment(apartmentId: Int) {
        val item = getApartmentUseCase.getApartment(apartmentId)
        _apartment.value = item
    }

    fun editApartment(apartment: Apartment) {
        editApartmentUseCase.editApartment(apartment)
        // TODO: add an implementation for clicking the like button
    }
}