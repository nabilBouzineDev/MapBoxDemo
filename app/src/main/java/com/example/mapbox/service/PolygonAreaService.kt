package com.example.mapbox.service

import com.example.mapbox.data.entity.AreaEntity
import com.example.mapbox.data.entity.PolygonPointEntity
import com.example.mapbox.data.relation.PolygonWithArea
import kotlinx.coroutines.flow.Flow

interface PolygonAreaService {
    fun getPolygonAreaNames(): Flow<List<AreaEntity>>

    suspend fun getPolygonByAreaName(areaName: String): List<PolygonWithArea>

    suspend fun insertPolygon(
        area: AreaEntity,
        polygonPoints: List<PolygonPointEntity>
    )
}