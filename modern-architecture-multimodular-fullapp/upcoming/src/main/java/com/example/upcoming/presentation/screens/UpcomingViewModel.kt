package com.example.upcoming.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.upcoming.domain.repository.UpcomingRepository
import com.example.utils.dateToMillis
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpcomingViewModel @Inject constructor(
    private val repository: UpcomingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UpcomingUiState())
    val uiState: StateFlow<UpcomingUiState> = _uiState.asStateFlow()

    init {
        getUpcoming()
    }

    fun getUpcoming() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }
            try {
                val items = repository.getUpcomingItems()
                val sortedItems = items.sortedBy { dateToMillis(it.date) }
                _uiState.update { it.copy(loading = false, items = sortedItems) }
            } catch (e: Exception) {
                _uiState.update { it.copy(loading = false, error = e.message) }
            }
        }
    }
}
