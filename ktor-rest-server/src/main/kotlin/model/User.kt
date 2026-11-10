package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val userName: String,
    val email: String,
    val password: String,
)

