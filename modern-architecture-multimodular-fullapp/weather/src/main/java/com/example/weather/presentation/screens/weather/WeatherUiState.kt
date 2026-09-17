package com.example.weather.presentation.screens

import com.example.weather.domain.model.Weather

data class WeatherUiState(
    val loading: Boolean = false,
    val items: List<Weather> = emptyList(),
    val error: String? = null
)
