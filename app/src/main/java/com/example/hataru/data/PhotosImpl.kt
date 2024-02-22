package com.example.hataru.data

import com.example.hataru.domain.PhotosRep
import com.example.hataru.domain.entity.Photos

class PhotosImpl(private var ser : PhotosService) : PhotosRep {
    override suspend fun getPhotos(): Photos {
        return ser.getPhotos().body() as Photos
    }
}