package com.example.multimodularmodernarchitecturefullapp.presenatation.screens.home

data class HomeUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val state: List<String> = emptyList()
)