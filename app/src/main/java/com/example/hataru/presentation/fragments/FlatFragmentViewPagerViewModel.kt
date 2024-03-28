import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hataru.domain.GetPhotosUseCase
import com.example.hataru.domain.entity.Photos
import com.example.hataru.domain.entity.RoomtypeWithPhotos
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class FlatFragmentViewPagerViewModel(private val photos : GetPhotosUseCase) : ViewModel() {

    private var _photos = MutableLiveData<Photos>()

    val photo: LiveData<Photos> get() = _photos

    init{
        viewModelScope.launch{
            _photos.value = photos.getPhotos()
        }
    }

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val favoriteFlatsCollection = firestore.collection(auth.currentUser?.uid.toString())

    fun changeLikedStage(roomtypeWithPhotos: RoomtypeWithPhotos?) {
        val roomId = roomtypeWithPhotos?.roomtype?.id
        val userId = auth.currentUser?.uid
        val favoriteFlatsCollection = firestore.collection(userId.toString())

        if (userId != null && roomId != null) {
            val favoriteFlatDocument = favoriteFlatsCollection.document(roomId)
            // Комнаты нет в избранных, добавляем ее
            favoriteFlatDocument.set(roomtypeWithPhotos)
                .addOnSuccessListener {
                    Log.d("TAG", "Room added to favorites: $roomId")
                }
                .addOnFailureListener { e ->
                    Log.w("TAG", "Error adding room to favorites", e)
                }


        }
    }

}