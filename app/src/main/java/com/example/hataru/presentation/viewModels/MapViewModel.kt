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


class MapViewModel(private val useCase: GetFlatsUseCase, private val photosCase: GetPhotosUseCase) :
    ViewModel() {
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var zoom: Float = 14.0f

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

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


    private val _visibleFlats = MutableLiveData<List<Roomtype>?>()
    val visibleFlats: LiveData<List<Roomtype>?> get() = _visibleFlats

    fun updateVisibleFlats(newFlats: List<Roomtype>?) {
        _visibleFlats.value = newFlats
        combinedLiveData.value = Pair(newFlats, url.value)
    }

    private val _flats = MutableLiveData<List<Roomtype>>()
    val flats: LiveData<List<Roomtype>> get() = _flats


    private var _url = MutableLiveData<List<RoomX>>()
    val url: LiveData<List<RoomX>?> get() = _url

    private val combinedLiveData = MediatorLiveData<Pair<List<Roomtype>?, List<RoomX>?>>()

    val combinedData: LiveData<Pair<List<Roomtype>?, List<RoomX>?>> = combinedLiveData

    init {
        combinedLiveData.addSource(flats) { flatResult ->
            combinedLiveData.value = Pair(flatResult, url.value)
        }

        combinedLiveData.addSource(url) { urlResult ->
            combinedLiveData.value = Pair(visibleFlats.value, urlResult)
        }

        viewModelScope.launch {
            val flatsDeferred = async { useCase.getFlats() }
            val photosDeferred = async { photosCase.getPhotos().rooms }

            val (flatsResult, photosResult) = flatsDeferred.await() to photosDeferred.await()

            _flats.value = flatsResult
            _url.value = photosResult
        }
    }


}