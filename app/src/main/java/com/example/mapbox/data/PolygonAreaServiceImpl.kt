package com.example.mapbox.data

import androidx.room.Transaction
import com.example.mapbox.data.entity.AreaEntity
import com.example.mapbox.data.entity.PolygonPointCrossRef
import com.example.mapbox.data.entity.PolygonPointEntity
import com.example.mapbox.service.PolygonAreaService

class PolygonAreaServiceImpl(
    private val polygonDao: PolygonDao
) : PolygonAreaService {

    override suspend fun getAllPolygons() = polygonDao.getAllPolygons()

    @Transaction
    override suspend fun insertPolygon(
        area: AreaEntity,
        polygonPoints: List<PolygonPointEntity>
    ) {
        polygonDao.insertArea(area)
        val ids = polygonDao.insertPolygonPoints(polygonPoints)

        val crossRefs = ids.map { pointId ->
            PolygonPointCrossRef(
                area.areaName,
                pointId
            )
        }
        polygonDao.insertPolygonCrossRefs(crossRefs)
    }
}