package com.example.mapbox.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.mapbox.data.entity.AreaEntity
import com.example.mapbox.data.entity.PolygonPointCrossRef
import com.example.mapbox.data.entity.PolygonPointEntity
import com.example.mapbox.data.relation.PolygonWithArea
import kotlinx.coroutines.flow.Flow

@Dao
interface PolygonDao {
    @Query("SELECT DISTINCT area.* FROM ${DatabaseConstant.AREA_TABLE} As area ORDER BY dateAdded DESC")
    fun getPolygonAreaNames(): Flow<List<AreaEntity>>

    @Query(
        """
        SELECT * FROM ${DatabaseConstant.AREA_TABLE} AS area
        LEFT JOIN ${DatabaseConstant.POLYGON_POINT_CROSS_TABLE} AS crossRef
        ON area.areaName = crossRef.areaName
        WHERE area.areaName = :areaName
        """
    )
    suspend fun getPolygonByAreaName(areaName: String): List<PolygonWithArea>

    @Upsert
    suspend fun insertPolygonPoints(points: List<PolygonPointEntity>): List<Long>

    @Upsert
    suspend fun insertArea(area: AreaEntity)

    @Upsert
    suspend fun insertPolygonCrossRefs(polygons: List<PolygonPointCrossRef>)
}