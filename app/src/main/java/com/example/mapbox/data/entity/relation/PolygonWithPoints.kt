package com.example.mapbox.data.entity.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.mapbox.data.entity.AreaEntity
import com.example.mapbox.data.entity.PolygonPointCrossRef
import com.example.mapbox.data.entity.PolygonPointEntity


data class PolygonWithPoints(
    @Embedded val polygon: AreaEntity,
    @Relation(
        parentColumn = "areaName",
        entityColumn = "pointId",
        associateBy = Junction(PolygonPointCrossRef::class)
    )
    val points: List<PolygonPointEntity>
)