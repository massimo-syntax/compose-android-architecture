package com.example.data.model.dto.emaildetails

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PayloadDto(
    @SerialName("email")
    val email: String?,
    @SerialName("from")
    val from: String?,
    @SerialName("timestamp")
    val timestamp: Long?
)
