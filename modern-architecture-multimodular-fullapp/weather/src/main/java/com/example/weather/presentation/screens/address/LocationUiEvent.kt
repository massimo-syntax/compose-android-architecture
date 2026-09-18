package com.example.weather.presentation.screens.address

sealed class LocationUiEvent{
    data class CityItemClicked(val cityName: String?) : LocationUiEvent()
}