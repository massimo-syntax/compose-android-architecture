package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class BlogRequest(
    val title: String,
    val content: String
)
