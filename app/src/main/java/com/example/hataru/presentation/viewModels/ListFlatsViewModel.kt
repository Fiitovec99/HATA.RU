package com.example.hataru.presentation.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hataru.domain.GetFlatsUseCase
import com.example.hataru.domain.GetPhotosUseCase
import com.example.hataru.domain.entity.RoomX
import com.example.hataru.domain.entity.Roomtype
import com.example.hataru.domain.entity.RoomtypeWithPhotos
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ListFlatsViewModel(private val rep: GetFlatsUseCase, private val photos: GetPhotosUseCase) : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

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
                } else {
                    // Комнаты нет в избранных, добавляем ее
                    favoriteFlatDocument.set(roomtypeWithPhotos)
                        .addOnSuccessListener {
                            Log.d("TAG", "Room added to favorites: $roomId")
                        }
                        .addOnFailureListener { e ->
                            Log.w("TAG", "Error adding room to favorites", e)
                        }
                }
            }.addOnFailureListener { e ->
                Log.w("TAG", "Error checking if room is in favorites", e)
            }
        }
    }










    private var _flats = MutableLiveData<List<Roomtype>>()
    val flats: LiveData<List<Roomtype>?> get() = _flats

    private var _url = MutableLiveData<List<RoomX>>()
    val url: LiveData<List<RoomX>?> get() = _url

    private val combinedLiveData = MediatorLiveData<Pair<List<Roomtype>?, List<RoomX>?>>()

    val combinedData: LiveData<Pair<List<Roomtype>?, List<RoomX>?>> = combinedLiveData

    init {
        combinedLiveData.addSource(flats) { flatResult ->
            combinedLiveData.value = Pair(flatResult, url.value)
        }

        combinedLiveData.addSource(url) { urlResult ->
            combinedLiveData.value = Pair(flats.value, urlResult)
        }

        viewModelScope.launch {
            val flatsDeferred = async { rep.getFlats() }
            val photosDeferred = async { photos.getPhotos().rooms }

            val (flatsResult, photosResult) = flatsDeferred.await() to photosDeferred.await()

            _flats.value = flatsResult
            _url.value = photosResult
        }
    }


}
