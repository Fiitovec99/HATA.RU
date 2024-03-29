package com.example.hataru.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize




@Parcelize
data class GeoData (
    var x: String? = null,
    var y: String? = null,
) : Parcelable
