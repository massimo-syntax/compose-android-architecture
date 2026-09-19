package com.example.weather.data.remote.dto.citynamecoordinates


import com.example.weather.domain.model.Coordinates
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CityNameCoordinates(
    @SerialName("query")
    val query: String = "",
    @SerialName("results")
    val results: List<Result> = listOf()
)




fun CityNameCoordinates.toDomain() =
    Coordinates(
        cityName = results[0].name,
        lat = results[0].latitude,
        lon = results[0].longitude
    )