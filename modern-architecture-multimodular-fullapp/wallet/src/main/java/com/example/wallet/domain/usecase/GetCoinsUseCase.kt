package com.example.wallet.domain.usecase

import com.example.wallet.domain.repository.CoinRepository
import jakarta.inject.Inject

class GetCoinsUseCase @Inject constructor(
    private val repository: CoinRepository
) {
    suspend operator fun invoke() = repository.getCoins()
}