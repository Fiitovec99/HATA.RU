package hataru.migration

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

data class Roomtypes (

    @JsonProperty("id") var id: String? = null,
    @JsonProperty("hotel_id") var hotelId: String? = null,
    @JsonProperty("parent_id") var parentId: String? = null,
    @JsonProperty("name") var name: String? = null,
    @JsonProperty("type") var type: String? = null,
    @JsonProperty("adults") var adults: String? = null,
    @JsonProperty("children") var children: Int? = null,
    @JsonProperty("children_age") var childrenAge: Int? = null,
    @JsonProperty("price") var price: Double,
    @JsonProperty("board") var board: String? = null,
    @JsonProperty("code") var code: String? = null,
    @JsonProperty("description") var description: String? = null,
    @JsonProperty("sort_order") var sortOrder: String? = null,
    @JsonProperty("country") var country: String? = null,
    @JsonProperty("city") var city: String? = null,
    @JsonProperty("address") var address: String? = null,
    @JsonProperty("city_eng") var cityEng: String? = null,
    @JsonProperty("address_eng") var addressEng: String? = null,
    @JsonProperty("postcode") var postcode: String? = null,
    @JsonProperty("gis_point") var gisPoint: String? = null,
    @JsonProperty("geo_data") var geoData: GeoData? = GeoData(),
    @JsonProperty("booking_url") var bookingUrl: String? = null,
    @JsonProperty("tripadvisor_url") var tripadvisorUrl: String? = null,
    @JsonProperty("provider_roomtype_settings") var providerRoomtypeSettings: ArrayList<String> = arrayListOf(),
    @JsonProperty("provider_roomtype_id") var providerRoomtypeId: String? = null,
    @JsonProperty("deleted") var deleted: String? = null,
    @JsonProperty("extra") var extra: Extra? = Extra(),
    @JsonProperty("subrooms") var subrooms: ArrayList<Subrooms> = arrayListOf(),
    @JsonProperty("rooms") var rooms: ArrayList<String> = arrayListOf()
) : Serializable


data class ExampleJson2KtKotlin(
    @JsonProperty("frontend_version") var frontendVersion: String? = null,
    @JsonProperty("roomtypes") var roomtypes: ArrayList<Roomtypes> = arrayListOf()
)

data class GeoData(
    @JsonProperty("x") var x: String? = null,
    @JsonProperty("y") var y: String? = null
)

data class Extra(
    @JsonProperty("set_guests") val setGuests: String? = null,
    @JsonProperty("children_ages") val childrenAges: List<Any>? = null
)


data class Subrooms(

    @JsonProperty("id") var id: String? = null,
    @JsonProperty("hotel_id") var hotelId: String? = null,
    @JsonProperty("parent_id") var parentId: String? = null,
    @JsonProperty("name") var name: String? = null,
    @JsonProperty("type") var type: String? = null,
    @JsonProperty("adults") var adults: String? = null,
    @JsonProperty("children") var children: String? = null,
    @JsonProperty("children_age") var childrenAge: String? = null,
    @JsonProperty("price") var price: String? = null,
    @JsonProperty("board") var board: String? = null,
    @JsonProperty("code") var code: String? = null,
    @JsonProperty("description") var description: String? = null,
    @JsonProperty("sort_order") var sortOrder: String? = null,
    @JsonProperty("country") var country: String? = null,
    @JsonProperty("city") var city: String? = null,
    @JsonProperty("address") var address: String? = null,
    @JsonProperty("city_eng") var cityEng: String? = null,
    @JsonProperty("address_eng") var addressEng: String? = null,
    @JsonProperty("postcode") var postcode: String? = null,
    @JsonProperty("gis_point") var gisPoint: String? = null,
    @JsonProperty("geo_data") var geoData: String? = null,
    @JsonProperty("booking_url") var bookingUrl: String? = null,
    @JsonProperty("tripadvisor_url") var tripadvisorUrl: String? = null,
    @JsonProperty("provider_roomtype_settings") var providerRoomtypeSettings: ArrayList<String> = arrayListOf(),
    @JsonProperty("provider_roomtype_id") var providerRoomtypeId: String? = null,
    @JsonProperty("deleted") var deleted: String? = null,
    @JsonProperty("extra") var extra: Extra? = Extra(),
    @JsonProperty("subrooms") var subrooms: String? = null,
    @JsonProperty("rooms") var rooms: ArrayList<String> = arrayListOf()

)

data class UserCredentials(val login: String?, val password: String?)
