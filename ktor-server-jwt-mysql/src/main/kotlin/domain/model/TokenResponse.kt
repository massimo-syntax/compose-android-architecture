package com.example.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val token: String,
    @SerialName("expires_at")
    val expiresAt: String
)
