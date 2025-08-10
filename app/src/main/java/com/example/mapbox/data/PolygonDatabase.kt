package com.example.mapbox.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mapbox.data.entity.AreaEntity
import com.example.mapbox.data.entity.PolygonPointCrossRef
import com.example.mapbox.data.entity.PolygonPointEntity

@Database(
    entities = [
        AreaEntity::class,
        PolygonPointEntity::class,
        PolygonPointCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PolygonDatabase : RoomDatabase() {

    abstract fun polygonDao(): PolygonDao

    companion object {
        const val DATABASE_NAME = "polygon_database"

        @Volatile
        private var instance: PolygonDatabase? = null

        fun getInstance(context: Context): PolygonDatabase {
            return instance ?: synchronized(this) {
                buildDatabase(context).also {
                    instance = it
                }
            }
        }

        private fun buildDatabase(context: Context): PolygonDatabase {
            return Room.databaseBuilder(context, PolygonDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration(false)
                .build()
        }
    }
}