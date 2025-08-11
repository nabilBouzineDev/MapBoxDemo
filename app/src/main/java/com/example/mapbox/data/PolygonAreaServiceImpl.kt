package com.example.mapbox.data

import androidx.room.Transaction
import com.example.mapbox.data.entity.AreaEntity
import com.example.mapbox.data.entity.PolygonPointCrossRef
import com.example.mapbox.data.entity.PolygonPointEntity
import com.example.mapbox.data.relation.PolygonWithArea
import com.example.mapbox.service.PolygonAreaService
import kotlinx.coroutines.flow.Flow

class PolygonAreaServiceImpl(
    private val polygonDao: PolygonDao
) : PolygonAreaService {

    override fun getPolygonAreaNames(): Flow<List<AreaEntity>> {
        return polygonDao.getPolygonAreaNames()
    }

    override suspend fun getPolygonByAreaName(areaName: String): List<PolygonWithArea> {
        return polygonDao.getPolygonByAreaName(areaName)
    }

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