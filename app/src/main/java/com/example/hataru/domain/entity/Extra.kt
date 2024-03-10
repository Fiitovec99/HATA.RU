package com.example.hataru.domain.entity

import android.os.Parcelable

import kotlinx.parcelize.Parcelize

@Parcelize
data class Extra(
   val setGuests: String? = null,
   val childrenAges: List<Int>? = null
) : Parcelable
