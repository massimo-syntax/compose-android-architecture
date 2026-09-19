package com.example.weather.presentation.screens.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.database.preferences.datasource.PreferencesDatasource
import com.example.utils.Resource
import com.example.weather.domain.model.Coordinates
import com.example.weather.domain.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: LocationRepository,
    private val preferences: PreferencesDatasource
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    init {
        getWeather()
    }

    fun getWeather() {
        // get location, then get weather
        viewModelScope.launch {
            _uiState.value = WeatherUiState(loading = true)

            val city = preferences.selectedCity.first()
            if (city == null){
                _uiState.value = WeatherUiState(error = "no selected cities")
                return@launch
            }
            _uiState.value = WeatherUiState(city = city)
            val coordinates = repository.getCoordinatesByCityName(city)
            if(coordinates is Resource.Success){
                val lat = coordinates.data?.lat ?: 0.0
                val lon = coordinates.data?.lon ?: 0.0
                _uiState.update {it.copy(
                    coordinates = Coordinates(city,lat,lon)
                )}
            }else{
                _uiState.update { it.copy(
                    error = "Error fetching data for city: $city"
                ) }
            }
        }
    }
}
