package com.example.mapbox.presentation

import com.example.mapbox.data.entity.AreaEntity
import com.example.mapbox.data.entity.PolygonPointEntity
import com.example.mapbox.presentation.utils.Constants
import com.mapbox.geojson.Point


data class PolygonUiState(
    val polygon: List<Point> = emptyList(),
    val selectedPoint: Point = Point.fromLngLat(
        Constants.DEFAULT_LONGITUDE,
        Constants.DEFAULT_LATITUDE
    ),
    val areaName: String = "",
    val openAreaDialogue: Boolean = false,
    val isLoading: Boolean = false,
    val errorState: String? = null
) {
    fun toAreaEntity() = AreaEntity(
        areaName = areaName
    )

    fun toPolygonPointEntities(): List<PolygonPointEntity> {
       return polygon.map { it.toPolygonPointEntity() }
    }

    private fun Point.toPolygonPointEntity() = PolygonPointEntity(
        latitude = this.latitude(),
        longitude = this.longitude()
    )
}