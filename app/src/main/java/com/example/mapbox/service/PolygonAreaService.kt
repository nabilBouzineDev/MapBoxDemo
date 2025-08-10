package com.example.mapbox.service

import com.example.mapbox.data.entity.AreaEntity
import com.example.mapbox.data.entity.PolygonPointEntity
import com.example.mapbox.data.entity.relation.PolygonWithPoints

interface PolygonAreaService {
    suspend fun getAllPolygons(): List<PolygonWithPoints>

    suspend fun insertPolygon(
        area: AreaEntity,
        polygonPoints: List<PolygonPointEntity>
    )
}