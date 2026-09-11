package com.example.wallet.domain.model

import com.example.database.local.model.CoinsEntity

data class Coin(
    val id: String = "",
    val isActive: Boolean = false,
    val isNew: Boolean = false,
    val name: String = "",
    val rank: Int = 0,
    val symbol: String = "",
    val type: String = ""
)

fun CoinsEntity.toDomain() =
    Coin(
        id = id,
        isActive = isActive,
        isNew = isNew,
        name = name,
        rank = rank,
        symbol = symbol,
        type = type
    )

fun Coin.toEntity() =
    CoinsEntity(
        id = id,
        isActive = isActive,
        isNew = isNew,
        name = name,
        rank = rank,
        symbol = symbol,
        type = type
    )