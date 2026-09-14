package com.example.multimodularmodernarchitecturefullapp.presenatation.screens.home

import com.example.upcoming.domain.model.Upcoming

data class HomeUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val nextUpcoming: Upcoming? = null
)
