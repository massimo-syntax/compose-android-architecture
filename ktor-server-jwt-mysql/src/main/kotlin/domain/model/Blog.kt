package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Blog(
    val id: Long,
    val title: String,
    val content: String,
    val author: User
)
