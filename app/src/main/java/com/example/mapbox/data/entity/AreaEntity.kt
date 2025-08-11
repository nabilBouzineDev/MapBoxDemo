package com.example.mapbox.data.entity

import androidx.room.Entity
import androidx.room.Index
import com.example.mapbox.data.DatabaseConstant

@Entity(
    tableName = DatabaseConstant.AREA_TABLE,
    primaryKeys = ["areaName"],
    indices = [Index(value = ["areaName"], unique = true)]
)
data class AreaEntity(
    val areaName: String,
    val dateAdded: Long = System.currentTimeMillis()
)