package com.example.weather.data.remote.dto.weather


import com.example.weather.domain.model.Weather
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherDto(
    @SerialName("current")
    val current: Current = Current(),
    @SerialName("latitude")
    val latitude: Double = 0.0,
    @SerialName("longitude")
    val longitude: Double = 0.0,
    @SerialName("timezone")
    val timezone: String = ""
)




fun WeatherDto.toDomain() =
    Weather(
        timezone = timezone,
        condition = current.condition,
        isDay = current.isDay,
        precipitationMm = current.precipitationMm,
        temperatureC = current.temperatureC,
        timestamp = current.timestamp,
        windSpeedKmh = current.windSpeedKmh
    )