package com.example.weather.presentation.screens.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.domain.model.Location
import com.example.weather.domain.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val repository: LocationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LocationUiState())
    val uiState: StateFlow<LocationUiState> = _uiState.asStateFlow()

    init {
        loadPopularLocations()
    }

    fun loadPopularLocations() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }
            repository.getPopularLocations()
                .catch { e ->
                    _uiState.update { it.copy(loading = false, error = e.message) }
                }
                .collect { locations ->
                    _uiState.update { it.copy(loading = false, items = locations) }
                }
        }
    }
}

data class LocationUiState(
    val loading: Boolean = false,
    val items: List<Location> = emptyList(),
    val error: String? = null
)
