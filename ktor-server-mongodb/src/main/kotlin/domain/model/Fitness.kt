package com.example.domain.model

import com.example.application.request.response.FitnessResponse
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Fitness(
    @BsonId
    val id: ObjectId,
    val exerciseType: String,
    val notes: String,
    val details: FitnessDetails
){
    fun toResponse() = FitnessResponse(
        id = id.toString(),
        exerciseType = exerciseType,
        notes = notes,
        details = details
    )
}

data class FitnessDetails(
    val durationMinutes: Int,
    val distance: Double,
    val caloriesBurned: Int
)
