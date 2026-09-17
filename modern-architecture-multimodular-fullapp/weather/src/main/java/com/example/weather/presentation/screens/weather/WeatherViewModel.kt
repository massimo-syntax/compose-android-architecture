package com.example.weather.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.domain.model.Weather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    init {
        getWeather()
    }

    fun getWeather() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }
            try {
                val sampleData = listOf(
                    Weather(id = 1, name = "Sunny"),
                    Weather(id = 2, name = "Cloudy"),
                    Weather(id = 3, name = "Rainy")
                )
                _uiState.update { it.copy(loading = false, items = sampleData) }
            } catch (e: Exception) {
                _uiState.update { it.copy(loading = false, error = e.message) }
            }
        }
    }
}
