package com.example.hataru.domain

import android.util.Log
import com.example.hataru.domain.entity.Photos
import com.example.hataru.domain.entity.Roomtype


class GetPhotosUseCase(private var rep: PhotosRep) {

    suspend fun getPhotos() : Photos{
        return rep.getPhotos()
    }
}