package com.example.hataru.domain

import com.example.hataru.domain.entity.Roomtype

class GetFlatsUseCase(private var rep : FlatsRep) {

    suspend fun getFlats() : List<Roomtype>{
        val flats = rep.getFlats()
        val flatsWithoutZeroCoordinates = flats.toList().filter{ x : Roomtype -> x.geo_data?.x?.toDouble() != 0.0 && x.geo_data?.y?.toDouble() != 0.0 }
        val pattern = Regex("^Ф[0-9]")
        return flatsWithoutZeroCoordinates.filter { x : Roomtype -> !pattern.containsMatchIn(x.name.toString()) }
    }
}
