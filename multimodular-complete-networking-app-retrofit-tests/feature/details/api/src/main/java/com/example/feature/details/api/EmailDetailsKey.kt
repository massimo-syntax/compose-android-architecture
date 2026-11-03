package com.example.feature.details.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class EmailDetailsKey(val id: String) : NavKey
