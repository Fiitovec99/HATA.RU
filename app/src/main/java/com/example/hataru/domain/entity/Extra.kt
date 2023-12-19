package com.example.hataru.domain.entity

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.parcelize.Parcelize

@Parcelize
data class Extra(
    @JsonProperty("set_guests") val setGuests: String? = null,
    @JsonProperty("children_ages") val childrenAges: List<Int>? = null
) : Parcelable
