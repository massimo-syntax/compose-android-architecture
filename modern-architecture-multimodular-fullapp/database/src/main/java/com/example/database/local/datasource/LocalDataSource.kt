package com.example.database.local.datasource

import com.example.database.local.model.CoinsEntity

interface LocalDataSource {
    fun insertCoinList(coins: List<CoinsEntity>)
    fun getCoinList(): List<CoinsEntity>
    fun deleteAllCoins()
}