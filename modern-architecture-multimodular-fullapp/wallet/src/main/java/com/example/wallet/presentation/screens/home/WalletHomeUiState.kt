package com.example.wallet.presentation.screens.home

import com.example.wallet.domain.model.Coin

data class WalletHomeUiState(
    val isLoading: Boolean = false,
    val coins: List<Coin> = emptyList(),
    val error: String? = null
)