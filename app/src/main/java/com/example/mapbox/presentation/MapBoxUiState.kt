package com.example.mapbox.presentation

import com.example.mapbox.presentation.utils.Constants
import com.mapbox.geojson.Point


data class MapBoxUiState(
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
}