package com.example.mapbox.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.mapbox.data.DatabaseConstant

@Entity(
    tableName = DatabaseConstant.POLYGON_POINT_CROSS_TABLE,
    primaryKeys = ["areaName", "polygonId"],
    foreignKeys = [
        ForeignKey(
            entity = AreaEntity::class,
            parentColumns = ["areaName"],
            childColumns = ["areaName"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PolygonPointEntity::class,
            parentColumns = ["polygonId"],
            childColumns = ["polygonId"],
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class PolygonPointCrossRef(
    val areaName: String,
    val polygonId: Long
)