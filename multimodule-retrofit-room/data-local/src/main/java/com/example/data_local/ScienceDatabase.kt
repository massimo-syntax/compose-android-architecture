package com.example.data_local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data_local.dao.ScienceDao
import com.example.data_local.entity.ScienceEntity

@Database(
    entities = [ScienceEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ScienceDatabase : RoomDatabase() {
    abstract fun scienceDao(): ScienceDao
}