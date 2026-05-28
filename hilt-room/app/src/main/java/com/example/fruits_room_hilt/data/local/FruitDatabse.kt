package com.example.fruits_room_hilt.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fruits_room_hilt.data.model.Fruit

@Database(entities = [Fruit::class] , version = 1, exportSchema = false)
abstract class FruitDatabase : RoomDatabase() {
    abstract fun fruitDao(): FruitDao
}