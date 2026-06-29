package com.example.data_local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "science")
data class ScienceEntity(
    @PrimaryKey
    val id: Int,
    val field: String,
    val concept: String,
    val researcher: String
)
