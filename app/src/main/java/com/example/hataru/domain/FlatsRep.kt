package com.example.hataru.domain

import com.example.hataru.domain.entity.Roomtype
import com.example.hataru.domain.entity.Root

interface FlatsRep {

    suspend fun getFlats() : List<Roomtype>
}