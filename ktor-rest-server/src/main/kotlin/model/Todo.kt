package com.example.model

import kotlinx.serialization.Serializable


@Serializable
data class Todo(
    val id: Long,
    val title: String
)

