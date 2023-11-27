package hataru.domain

class EditApartmentUseCase(private val apartmentListRepository: ApartmentListRepository) {

    fun editApartment(apartment: Apartment) {
        apartmentListRepository.editApartment(apartment)
    }
}