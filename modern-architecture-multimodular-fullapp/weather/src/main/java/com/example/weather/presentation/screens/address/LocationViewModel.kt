package com.example.weather.presentation.screens.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.database.preferences.datasource.PreferencesDatasource
import com.example.utils.Resource
import com.example.weather.domain.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val repository: LocationRepository,
    private val cityPreferencesDatasource: PreferencesDatasource
) : ViewModel() {

    private val _uiState = MutableStateFlow(LocationUiState())
    val uiState: StateFlow<LocationUiState> = _uiState.asStateFlow()

    init {
        loadPopularLocations()
        loadLocationPreferences()
    }

    private fun loadPopularLocations() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }
            repository.getPopularLocations()
                .onEach { result ->
                    when(result){
                        is Resource.Loading -> {
                            _uiState.update { it.copy(loading = true) }
                        }
                        is Resource.Error -> {
                            _uiState.update { it.copy(loading = false, error = result.message ) }
                        }
                        is Resource.Success -> {
                            _uiState.update { it.copy(loading = false, locations = result.data ?: emptyList()) }
                        }
                    }
                }.launchIn(viewModelScope)
        }
    }

    private fun loadLocationPreferences(){
        viewModelScope.launch {
            cityPreferencesDatasource.selectedCity.onEach { location ->
                _uiState.update { it.copy(selectedLocation = location) }
            }.launchIn(viewModelScope)
        }
    }



    fun event(event: LocationUiEvent){
        when(event){
            is LocationUiEvent.CityItemClicked -> {
                // toggle or select other city
                viewModelScope.launch(Dispatchers.IO) {
                    val city =
                        if(event.cityName == _uiState.value.selectedLocation) null
                        else event.cityName
                    cityPreferencesDatasource.toggleSelectCity(city)
                }
            }
        }
    }


}


