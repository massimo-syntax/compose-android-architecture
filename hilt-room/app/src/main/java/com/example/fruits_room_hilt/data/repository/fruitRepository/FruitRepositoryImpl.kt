package com.example.fruits_room_hilt.data.repository.fruitRepository

import com.example.fruits_room_hilt.data.local.FruitDao
import com.example.fruits_room_hilt.data.model.Fruit
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow


// without hilt -> fruit dao is a dependency
//class FruitRepositoryImpl (private val fruitDao: FruitDao): FruitRepository {

/*
DAO is an interface. So this code cannot compile because we cannot constructor inject an interface.
However we can provide binding information to hilt, telling hilt how to provide an instance of the interface.
Constructor injection is also not possible if we do not own the class. We do not own Room Database, it is an external class provided to us.
Hence we cannot use its constructor to inject it.

 In this case, we need to use @Module -> it is in the di package.
 */
class FruitRepositoryImpl @Inject constructor(private val fruitDao: FruitDao): FruitRepository {

    override suspend fun insertFruit(fruit: Fruit) =
        fruitDao.insertFruit(fruit)

    override fun getAllFruits(): Flow<List<Fruit>> =
        fruitDao.getAllFruits()

}

