package com.example.fruits_room_hilt.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.fruits_room_hilt.data.model.Fruit
import kotlinx.coroutines.flow.Flow

@Dao
interface FruitDao {

    @Insert
    suspend fun insertFruit(fruit: Fruit)

    @Query("SELECT * FROM Fruit")
    fun getAllFruits() : Flow<List<Fruit>>
}