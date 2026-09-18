package com.example.multimodularmodernarchitecturefullapp.presenatation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.database.preferences.datasource.PreferencesDatasource
import com.example.upcoming.domain.model.Upcoming
import com.example.upcoming.domain.repository.UpcomingRepository
import com.example.utils.dateToMillis
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val upcomingRepository: UpcomingRepository,
    private val cityPreferencesDatasource: PreferencesDatasource
): ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init{
        getNextUpcoming()
        listenLocationPreferences()
    }

    fun getNextUpcoming(){
        viewModelScope.launch {
            try {
                val items = upcomingRepository.getUpcomingItems()
                val sortedItems = items.sortedBy { dateToMillis(it.date) }
                val item = sortedItems.firstOrNull { dateToMillis(it.date) > System.currentTimeMillis() }

                if (item == null) {
                    _uiState.value = uiState.value.copy(
                        loading = false,
                        nextUpcoming = Upcoming(0, "", "Nothing Upcoming", "Nothing Upcoming", "", "")
                    )
                } else {
                    _uiState.value = uiState.value.copy(
                        loading = false,
                        nextUpcoming = item
                    )
                }
            } catch (e: Exception) {
                _uiState.value = uiState.value.copy(
                    loading = false,
                    error = e.message
                )
            }
        }
    }

    fun listenLocationPreferences(){
        viewModelScope.launch {
            cityPreferencesDatasource.selectedCity.collect { location ->
                _uiState.update { it.copy(selectedLocation = location) }
            }
        }
    }

}
