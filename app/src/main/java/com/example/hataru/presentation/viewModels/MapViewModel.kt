package com.example.hataru.presentation.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hataru.domain.GetFlatsUseCase
import com.example.hataru.domain.entity.Roomtype
import com.example.hataru.domain.entity.Roomtypes
import kotlinx.coroutines.launch


class MapViewModel(private val useCase : GetFlatsUseCase) : ViewModel() {
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var zoom: Float = 14.0f


    private val _visibleFlats = MutableLiveData<List<Roomtype>?>()
    val visibleFlats: LiveData<List<Roomtype>?> get() = _visibleFlats

    fun updateVisibleFlats(newFlats: List<Roomtype>?) {
        _visibleFlats.value = newFlats
    }



    private val _flats = MutableLiveData<List<Roomtype>>()
    val flats : LiveData<List<Roomtype>> get() = _flats

    init {
        viewModelScope.launch {
            _flats.value = useCase.getFlats()
            Log.d("FLATTTTTTTTTS",useCase.getFlats().toString())

        }
    }

}