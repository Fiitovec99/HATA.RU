package hataru.domain

data class Apartment(
    val address: String,
    val description: String,
    val people: Int,
    val price: Int,
    val area: Double,
    val liked: Boolean,
    var id: Int = UNDEFIND_ID
) {

    companion object{
        const val UNDEFIND_ID = -1
    }
}
