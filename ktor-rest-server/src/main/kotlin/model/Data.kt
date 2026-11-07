package com.example.model

import kotlinx.serialization.Serializable


@Serializable
data class Data(
    val id: Int,
    val name: String
)