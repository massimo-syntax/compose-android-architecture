package com.example.fruits_room_hilt.data.repository.fruitRepository

import com.example.fruits_room_hilt.data.model.Fruit
import kotlinx.coroutines.flow.Flow

interface FruitRepository {
    suspend fun insertFruit(fruit: Fruit)
    fun getAllFruits(): Flow<List<Fruit>>
}