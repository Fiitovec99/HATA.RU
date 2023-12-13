package com.example.hataru.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hataru.domain.entity.Roomtypes
import com.yandex.mapkit.geometry.Point


class MapViewModel : ViewModel() {

    //////////////////
    private val _cameraPosition = MutableLiveData<Pair<Point, Float>>()
    val cameraPosition: LiveData<Pair<Point, Float>> get() = _cameraPosition

    fun updateCameraPosition(pair: Pair<Point, Float>) {
        _cameraPosition.value = pair
    }
    ////////////////////////


    private val _visibleFlats = MutableLiveData<List<Roomtypes>>()
    val visibleFlats: LiveData<List<Roomtypes>> get() = _visibleFlats

    // Function to update visible flats
    fun updateVisibleFlats(newFlats: List<Roomtypes>) {
        _visibleFlats.value = newFlats
    }
    /////////////////////////

}