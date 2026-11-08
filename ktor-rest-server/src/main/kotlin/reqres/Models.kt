package com.example.reqres

import kotlinx.serialization.Serializable


// Data models
@Serializable
data class User(
    val id: Long? = null,
    val name: String,
    val email: String,
    val role: String = "user"
)

@Serializable
data class CreateUserRequest(
    val name: String,
    val email: String,
    val password: String
)

@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: String? = null
)