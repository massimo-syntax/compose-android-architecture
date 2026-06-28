package com.example.data_remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ScienceDto(
    @param:Json(name = "id")
    val id: Int,
    @param:Json(name = "field")
    val field: String,
    @param:Json(name = "concept")
    val concept: String,
    @param:Json(name = "difficulty")
    val difficulty: String,
    @param:Json(name = "researcher")
    val researcher: String
)
