package com.example.weather.presentation.screens.address

import com.example.weather.domain.model.Location

data class LocationUiState(
    val loading: Boolean = false,
    val locations: List<Location> = emptyList(),
    val selectedLocation: String? = null,
    val error: String? = null
)