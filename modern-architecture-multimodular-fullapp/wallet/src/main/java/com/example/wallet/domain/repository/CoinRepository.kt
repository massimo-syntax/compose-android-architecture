package com.example.wallet.domain.repository

import com.example.utils.Resource
import com.example.wallet.domain.model.Coin
import com.example.wallet.domain.model.CoinDetail
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    suspend fun getCoins(): Flow<Resource<List<Coin>>>
    suspend fun getCoinById(coinId: String): Flow<Resource<CoinDetail>>
}
