package com.example.hataru.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hataru.domain.GetPhotosUseCase
import com.example.hataru.domain.entity.Photos
import com.example.hataru.domain.entity.RoomX
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FlatBottomSheetViewModel(private val photosCase: GetPhotosUseCase):ViewModel() {


    private var _url = MutableLiveData<Photos>()
    val url: LiveData<Photos> get() = _url



    init{
        viewModelScope.launch{
            _url.value = photosCase.getPhotos()


        }
    }




}