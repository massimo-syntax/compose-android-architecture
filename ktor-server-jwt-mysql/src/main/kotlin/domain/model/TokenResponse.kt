package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val token: String
)
