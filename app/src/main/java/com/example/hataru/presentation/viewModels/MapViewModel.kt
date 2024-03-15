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
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class MapViewModel(private val useCase: GetFlatsUseCase, private val photosCase: GetPhotosUseCase) :
    ViewModel() {
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var zoom: Float = 14.0f


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