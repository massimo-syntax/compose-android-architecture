package com.example.domain.ports

import com.example.domain.model.Fitness
import org.bson.BsonValue
import org.bson.types.ObjectId

interface FitnessRepository {
    suspend fun insertOne(fitness: Fitness): BsonValue?
    suspend fun deleteById(objectId: ObjectId): Long
    suspend fun findById(objectId: ObjectId): Fitness?
    suspend fun updateOne(objectId: ObjectId, fitness: Fitness): Long
}
