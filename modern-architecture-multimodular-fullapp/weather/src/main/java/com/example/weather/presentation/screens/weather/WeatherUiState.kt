package com.example.weather.presentation.screens.weather

import com.example.weather.domain.model.Coordinates
import com.example.weather.domain.model.Weather

data class WeatherUiState(
    val loading: Boolean = false,
    val city: String? = null,
    val coordinates: Coordinates? = null,
    val weather: Weather? = null,
    val error: String? = null
)

private data class Weather(
    val weather: String = "Raining with sun"
)