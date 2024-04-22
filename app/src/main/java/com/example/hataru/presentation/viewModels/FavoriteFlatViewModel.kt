import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hataru.domain.entity.GeoData
import com.example.hataru.domain.entity.Photo
import com.example.hataru.domain.entity.Room
import com.example.hataru.domain.entity.Roomtype
import com.example.hataru.domain.entity.RoomtypeWithPhotos
import com.example.hataru.domain.entity.Subroom
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class FavoriteFlatViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val favoriteFlatsCollection = firestore.collection(auth.currentUser?.uid.toString())

    private val _favoriteFlats = MutableLiveData<List<RoomtypeWithPhotos>>()
    val favoriteFlats: LiveData<List<RoomtypeWithPhotos>> get() = _favoriteFlats

    init {
        auth.currentUser?.uid?.let { userId ->
            favoriteFlatsCollection
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        // Обработайте ошибку
                        Log.e("FavoriteFlatViewModel", "Error fetching favorite flats: $error")
                        return@addSnapshotListener
                    }

                    val favoriteFlatsList = mutableListOf<RoomtypeWithPhotos>()
                    snapshot?.documents?.forEach { document ->
                        val data = document.data
                        val roomtypeMap = data?.get("roomtype") as HashMap<*, *>
                        val photosList = data["photos"] as List<HashMap<*, *>>


                        val geoDataMap = roomtypeMap["geo_data"] as HashMap<*, *>
                        val geoData = GeoData(
                            x = geoDataMap["x"] as String?,
                            y = geoDataMap["y"] as String?
                        )

                        val roomtype = Roomtype(
                            id = roomtypeMap["id"] as? String,
                            hotel_id = roomtypeMap["hotel_id"] as? String,
                            parent_id = roomtypeMap["parent_id"] as? String,
                            name = roomtypeMap["name"] as? String,
                            type = roomtypeMap["type"] as? String,
                            adults = roomtypeMap["adults"] as? String,
                            children = roomtypeMap["children"] as? String,
                            price = roomtypeMap["price"]?.toString() ?: "",
                            board = roomtypeMap["board"] as? String,
                            description = roomtypeMap["description"] as? String,
                            sort_order = roomtypeMap["sort_order"] as? String,
                            country = roomtypeMap["country"] as? String,
                            city = roomtypeMap["city"] as? String,
                            city_eng = roomtypeMap["city_eng"] as? String,
                            address = roomtypeMap["address"] as? String,
                            address_eng = roomtypeMap["address_eng"] as? String,
                            postcode = roomtypeMap["postcode"] as? String,
                            geo_data = geoData, 
                            subrooms = roomtypeMap["subrooms"] as? ArrayList<Subroom>,
                            rooms = roomtypeMap["rooms"] as? ArrayList<Room>
                        )


                        // Инициализация списка фотографий
                        val photos = photosList.map { photoMap ->
                            Photo(
                                id = photoMap["id"] as? Double ?: 0.0,
                                name = photoMap["name"] as? String ?: "",
                                fileName = photoMap["fileName"] as? String ?: "",
                                mimeType = photoMap["mimeType"] as? String ?: "",
                                size = photoMap["size"] as? Double ?: 0.0,
                                accountId = photoMap["accountId"] as? Double ?: 0.0,
                                createDate = photoMap["createDate"] as? Date ?: Date(),
                                updateDate = photoMap["updateDate"] as? Date ?: Date(),
                                roomtypeId = photoMap["roomtypeId"] as? Double ?: 0.0,
                                order = photoMap["order"] as? Double ?: 0.0,
                                url = photoMap["url"] as? String ?: "",
                                thumb = photoMap["thumb"] as? String ?: ""
                            )
                        }

                        Log.d("ROOMTYPE", roomtype.toString())
                        Log.d("photos", photos.toString())
                        // Добавление объекта RoomtypeWithPhotos в список
                        favoriteFlatsList.add(RoomtypeWithPhotos(roomtype, photos))
                    }
                    _favoriteFlats.value = favoriteFlatsList

                }
        }


    }
    fun changeLikedStage(roomtypeWithPhotos: RoomtypeWithPhotos?) {
        val roomId = roomtypeWithPhotos?.roomtype?.id
        val userId = auth.currentUser?.uid
        val favoriteFlatsCollection = firestore.collection(userId.toString())

        if (userId != null && roomId != null) {
            val favoriteFlatDocument = favoriteFlatsCollection.document(roomId)

            // Проверяем, есть ли комната в избранных
            favoriteFlatDocument.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // Комната уже в избранных, значит удаляем ее
                    favoriteFlatDocument.delete()
                        .addOnSuccessListener {
                            Log.d("TAG", "Room removed from favorites: $roomId")
                        }
                        .addOnFailureListener { e ->
                            Log.w("TAG", "Error removing room from favorites", e)
                        }
                }
            }.addOnFailureListener { e ->
                Log.w("TAG", "Error checking if room is in favorites", e)
            }
        }
    }
}