package com.example.hataru.domain

import android.util.Log
import com.example.hataru.domain.entity.Roomtype

class GetFlatsUseCase(private var rep : FlatsRep) {


    suspend fun getFlats() : List<Roomtype>{
        val flats = rep.getFlats()

        if (flats != null) {
            return flats.toList().filter{ x : Roomtype -> x.geo_data?.x?.toDouble() != 0.0 && x.geo_data?.y?.toDouble() != 0.0 }
        } else {
            Log.d("as",flats.toString())
            return listOf()
        }
    }
}
