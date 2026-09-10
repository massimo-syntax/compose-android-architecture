package com.example.wallet.presentation.screens.coindetails

import com.example.wallet.domain.model.CoinDetail

data class CoinDetailsUiState(
    val isLoading: Boolean = false,
    val coin: CoinDetail? = null,
    val error: String? = null
)