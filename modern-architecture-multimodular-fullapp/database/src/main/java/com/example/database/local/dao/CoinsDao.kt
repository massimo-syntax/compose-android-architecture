package com.example.database.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.example.database.local.DbConst
import com.example.database.local.model.CoinsEntity


@Dao
interface CoinsDao : BaseDao<CoinsEntity> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(coins:List<CoinsEntity>)

    @Query("SELECT * FROM ${DbConst.COIN_LISTS}")
    fun getCoins(): List<CoinsEntity>

    @Query("DELETE from ${DbConst.COIN_LISTS}")
    fun deleteAll()
}