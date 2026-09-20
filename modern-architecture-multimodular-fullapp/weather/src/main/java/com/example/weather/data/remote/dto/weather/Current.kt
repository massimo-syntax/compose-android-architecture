package com.example.weather.data.remote.dto.weather


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Current(
    @SerialName("apparentTemperatureC")
    val apparentTemperatureC: Double = 0.0,
    @SerialName("condition")
    val condition: String = "",
    @SerialName("isDay")
    val isDay: Boolean = false,
    @SerialName("precipitationMm")
    val precipitationMm: Int = 0,
    @SerialName("temperatureC")
    val temperatureC: Double = 0.0,
    @SerialName("timestamp")
    val timestamp: String = "",
    @SerialName("weatherCode")
    val weatherCode: Int = 0,
    @SerialName("windDirectionDeg")
    val windDirectionDeg: Int = 0,
    @SerialName("windSpeedKmh")
    val windSpeedKmh: Double = 0.0
)