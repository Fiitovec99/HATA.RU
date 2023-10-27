package com.example.hataru.presentation.forMap

import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition

object GeometryProvider {

    // позиция ростова
    val startPosition =  CameraPosition(Point(47.222078, 39.720358), 14.0f, 0.0f, 0.0f)


    fun getFlats() : List<flat>{
        var list = mutableListOf<flat>()
        for (i in 0..20){
            var random = (1..3).random()
            list.add(flat(random, clusterizedPoints[i]))
        }
            return list
    }

    val clusterizedPoints = listOf(
        47.225873 to 39.719055,
        59.938961 to 30.328576,
        59.938152 to 30.336384,
        59.934600 to 30.335049,
        59.938386 to 30.329092,
        59.938495 to 30.330557,
        59.938854 to 30.332325,
        59.937930 to 30.333767,
        59.937766 to 30.335208,
        59.938203 to 30.334316,
        47.245484 to 39.761690,
        47.245487 to 39.761691,
        47.245486 to 39.761692,
        47.218462 to 39.705831,
        47.234235 to 39.709966,
        47.223471 to 39.718604,
        47.228312 to 39.745331,
        47.234215 to 39.733914,
        47.221786 to  39.726932,
        47.227753 to 39.731264,
    ).map { (lat, lon) -> Point(lat, lon) }

}
