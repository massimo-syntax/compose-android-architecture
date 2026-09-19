package com.example.weather.domain.model

class Location(
    val name: String,
    val lat: Long,
    val lon: Long,
    val metadata: String? = null
)