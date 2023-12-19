package com.example.hataru.domain.entity

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.parcelize.Parcelize

@Parcelize
data class GeoData(
    @JsonProperty("x") var x: String? = null,
    @JsonProperty("y") var y: String? = null
) : Parcelable
