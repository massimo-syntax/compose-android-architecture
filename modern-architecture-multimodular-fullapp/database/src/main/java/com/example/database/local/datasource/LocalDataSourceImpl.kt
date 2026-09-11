package com.example.database.local.datasource

import com.example.database.local.dao.CoinsDao
import com.example.database.local.model.CoinsEntity
import jakarta.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val coinsDao: CoinsDao
) : LocalDataSource {
    override fun insertCoinList(coins: List<CoinsEntity>) {
        coinsDao.insert(coins)
    }

    override fun getCoinList(): List<CoinsEntity> = coinsDao.getCoins()

    override fun deleteAllCoins() {
        coinsDao.deleteAll()
    }
}