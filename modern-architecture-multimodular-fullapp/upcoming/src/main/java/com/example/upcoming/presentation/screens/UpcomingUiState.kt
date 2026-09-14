package com.example.upcoming.presentation.screens

import com.example.upcoming.domain.model.Upcoming

data class UpcomingUiState(
    val loading: Boolean = true,
    val items: List<Upcoming> = emptyList(),
    val error: String? = null
)
