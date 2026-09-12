package com.example.wallet.presentation.screens.coindetails

import com.example.wallet.domain.model.CoinDetail
import com.example.wallet.domain.model.CoinTicker

data class CoinDetailsUiState(
    val isLoading: Boolean = false,
    val coin: CoinDetail? = null,
    var ticker: CoinTicker? = null,
    val error: String? = null
)