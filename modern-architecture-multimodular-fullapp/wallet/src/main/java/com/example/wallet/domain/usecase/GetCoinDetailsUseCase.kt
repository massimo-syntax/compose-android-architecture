package com.example.wallet.domain.usecase

import com.example.wallet.domain.repository.CoinRepository
import javax.inject.Inject

class GetCoinDetailsUseCase @Inject constructor (
    private val repository: CoinRepository
) {
    suspend operator fun invoke(coinId: String) = repository.getCoinById(coinId)
}