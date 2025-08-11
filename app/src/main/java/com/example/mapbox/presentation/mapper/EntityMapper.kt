package com.example.mapbox.presentation.mapper

import com.example.mapbox.data.entity.PolygonPointEntity
import com.mapbox.geojson.Point

fun List<PolygonPointEntity>.toPoints(): List<Point> {
    return this.map {
        Point.fromLngLat(
            it.longitude,
            it.latitude
        )
    }
}