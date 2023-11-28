package hataru.domain

class GetApartmentUseCase(private val apartmentListRepository: ApartmentListRepository) {

    fun getApartment(apartmentId: Int): Apartment {
        return apartmentListRepository.getApartment(apartmentId)
    }
}