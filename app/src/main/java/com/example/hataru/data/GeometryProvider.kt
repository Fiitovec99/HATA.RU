package com.example.hataru.data

import com.example.hataru.domain.entity.Roomtype
import com.example.hataru.domain.entity.Root
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition

object GeometryProvider {

    // позиция ростова
    val startPosition =  CameraPosition(Point(47.222078, 39.720358), 14.0f, 0.0f, 0.0f)


    var listFlats : List<Roomtype> = listOf()

}
