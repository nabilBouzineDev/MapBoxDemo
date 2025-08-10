package com.example.mapbox.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mapbox.data.DatabaseConstant

@Entity(tableName = DatabaseConstant.AREA_TABLE)
data class AreaEntity(
    @PrimaryKey val areaName: String,
)