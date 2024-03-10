package com.example.hataru.presentation.viewModels

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

class ListFlatsViewModel(private val rep: GetFlatsUseCase, private val photos: GetPhotosUseCase) : ViewModel() {

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
