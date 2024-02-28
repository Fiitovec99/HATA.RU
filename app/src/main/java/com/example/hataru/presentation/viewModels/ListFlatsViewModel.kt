package com.example.hataru.presentation.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hataru.data.ApartmentListRepositoryImpl
import com.example.hataru.domain.Apartment
import com.example.hataru.domain.EditApartmentUseCase
import com.example.hataru.domain.GetApartmentListUseCase
import com.example.hataru.domain.GetFlatsUseCase
import com.example.hataru.domain.GetPhotosUseCase
import com.example.hataru.domain.entity.Roomtype
import kotlinx.coroutines.launch

class ListFlatsViewModel(private val rep : GetFlatsUseCase,private val photos : GetPhotosUseCase) : ViewModel() {

    private var _flats = MutableLiveData<List<Roomtype>>()

    val flats: LiveData<List<Roomtype>?> get() = _flats

    init{
        viewModelScope.launch{
            _flats.value = rep.getFlats()
            //Log.d("asdasd",photos.getPhotos().rooms.toString())
        }
    }



}