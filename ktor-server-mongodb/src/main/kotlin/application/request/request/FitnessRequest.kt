package com.example.application.request.request

import com.example.domain.model.Fitness
import com.example.domain.model.FitnessDetails
import org.bson.types.ObjectId


data class FitnessRequest(
    val id: String,
    val exerciseType: String,
    val notes: String,
    val details: FitnessDetails
){
    fun toDomain() = Fitness(
        id = ObjectId(),
        exerciseType = exerciseType,
        notes = notes,
        details = details
    )
}
