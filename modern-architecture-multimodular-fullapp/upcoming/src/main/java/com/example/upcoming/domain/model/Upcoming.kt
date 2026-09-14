package com.example.upcoming.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Upcoming(
    val id: Int,
    @SerialName("created_at")
    val createdAt: String,
    val name: String,
    val description: String?,
    val date: String,
    val extra: String?,
)
