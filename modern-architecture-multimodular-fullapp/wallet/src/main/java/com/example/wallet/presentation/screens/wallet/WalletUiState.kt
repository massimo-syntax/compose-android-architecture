package com.example.wallet.presentation.screens.wallet

data class WalletUiState(
    val balance: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String? = null
)
