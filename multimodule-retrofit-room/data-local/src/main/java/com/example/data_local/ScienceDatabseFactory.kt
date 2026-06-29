package com.example.data_local

import android.content.Context
import androidx.room.Room

object DatabaseFactory {

    fun create(context: Context): ScienceDatabase {
        return Room.databaseBuilder(
            context,
            ScienceDatabase::class.java,
            "movie_db"
        ).build()
    }

    /* later
    fun createTest(context: Context): ScienceDatabase{
        return Room.inMemoryDatabaseBuilder(
            context,
            ScienceDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
    }
    */
}