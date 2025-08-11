package com.example.mapbox.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mapbox.data.DatabaseConstant

@Entity(tableName = DatabaseConstant.POLYGON_POINT_TABLE)
data class PolygonPointEntity(
    @PrimaryKey(autoGenerate = true) val polygonId: Long = 0,
    val latitude: Double,
    val longitude: Double,
)