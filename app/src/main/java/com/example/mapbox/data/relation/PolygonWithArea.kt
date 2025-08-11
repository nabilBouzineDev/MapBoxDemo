package com.example.mapbox.data.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.mapbox.data.entity.AreaEntity
import com.example.mapbox.data.entity.PolygonPointCrossRef
import com.example.mapbox.data.entity.PolygonPointEntity


data class PolygonWithArea(
    @Embedded val area: AreaEntity,
    @Relation(
        parentColumn = "areaName",
        entityColumn = "polygonId",
        associateBy = Junction(PolygonPointCrossRef::class)
    )
    val points: List<PolygonPointEntity>
)