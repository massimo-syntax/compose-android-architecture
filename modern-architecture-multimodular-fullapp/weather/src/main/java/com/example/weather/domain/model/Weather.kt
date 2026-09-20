package com.example.weather.domain.model

data class Weather(
    val timezone: String,
    val condition: String = "",
    val isDay: Boolean = false,
    val precipitationMm: Int = 0,
    val temperatureC: Double = 0.0,
    val timestamp: String = "",
    val windSpeedKmh: Double = 0.0
)
