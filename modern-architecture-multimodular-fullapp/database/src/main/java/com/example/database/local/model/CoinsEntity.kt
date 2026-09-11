package com.example.database.local.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.example.database.local.DbConst

@Entity(tableName = DbConst.COIN_LISTS)
data class CoinsEntity(
    @PrimaryKey
    val id: String = "",
    val isActive: Boolean = false,
    val isNew: Boolean = false,
    val name: String = "",
    val rank: Int = 0,
    val symbol: String = "",
    val type: String = ""
)
