package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserAuthRequest(
    val username: String,
    val password: String
)