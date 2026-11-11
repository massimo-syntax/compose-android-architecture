package com.example.application.request.response

import com.example.domain.model.FitnessDetails


data class FitnessResponse(
    val id: String,
    val exerciseType: String,
    val notes: String,
    val details: FitnessDetails
)
