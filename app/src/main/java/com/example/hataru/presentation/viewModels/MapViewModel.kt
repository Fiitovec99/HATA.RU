package com.example.hataru.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.hataru.domain.entity.Roomtypes


class MapViewModel : ViewModel() {
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var zoom: Float = 14.0f


    private val _visibleFlats = MutableLiveData<List<Roomtypes>>()
    val visibleFlats: LiveData<List<Roomtypes>> get() = _visibleFlats

    // Function to update visible flats
    fun updateVisibleFlats(newFlats: List<Roomtypes>) {
        _visibleFlats.value = newFlats
    }


}