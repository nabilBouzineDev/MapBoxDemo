package com.example.mapbox.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.mapbox.data.entity.AreaEntity
import com.example.mapbox.data.entity.PolygonPointCrossRef
import com.example.mapbox.data.entity.PolygonPointEntity
import com.example.mapbox.data.entity.relation.PolygonWithPoints

@Dao
interface PolygonDao {
    @Query(
        """
        SELECT * FROM ${DatabaseConstant.AREA_TABLE} AS polygon
        LEFT JOIN ${DatabaseConstant.POLYGON_POINT_CROSS_TABLE} AS crossRef
        ON polygon.areaName = crossRef.areaName
    """
    )
    suspend fun getAllPolygons(): List<PolygonWithPoints>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPolygonPoints(points: List<PolygonPointEntity>): List<Long>

    @Upsert
    suspend fun insertArea(area: AreaEntity)

    @Upsert
    suspend fun insertPolygonCrossRefs(polygons: List<PolygonPointCrossRef>)
}