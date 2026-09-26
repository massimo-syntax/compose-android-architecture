package com.example.multimodularmodernarchitecturefullapp.presenatation.screens.home

import com.example.upcoming.domain.model.Upcoming
import com.example.weather.domain.model.Weather

data class HomeUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val nextUpcoming: Upcoming? = null,
    val selectedLocation: String? = null,
    val weather: Weather? = null,
)
