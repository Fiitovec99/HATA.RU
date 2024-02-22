package com.example.hataru.domain

import com.example.hataru.domain.entity.Photos

interface PhotosRep {
    suspend fun getPhotos() : Photos
}