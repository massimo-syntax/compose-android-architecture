package com.example.weather.data.remote.dto.citynamecoordinates


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Result(
    @SerialName("admin1")
    val admin1: String = "",
    @SerialName("country")
    val country: String = "",
    @SerialName("countryCode")
    val countryCode: String = "",
    @SerialName("latitude")
    val latitude: Double = 0.0,
    @SerialName("longitude")
    val longitude: Double = 0.0,
    @SerialName("name")
    val name: String = "",
    @SerialName("population")
    val population: Int = 0,
    @SerialName("timezone")
    val timezone: String = ""
)

