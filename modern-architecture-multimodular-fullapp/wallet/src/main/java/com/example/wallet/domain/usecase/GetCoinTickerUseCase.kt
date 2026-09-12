package com.example.wallet.domain.usecase

import com.example.wallet.domain.repository.CoinRepository
import jakarta.inject.Inject

class GetCoinTickerUseCase @Inject constructor(
    private val repository: CoinRepository
) {
    suspend operator fun invoke(id: String) = repository.getCoinTicker(id)
}