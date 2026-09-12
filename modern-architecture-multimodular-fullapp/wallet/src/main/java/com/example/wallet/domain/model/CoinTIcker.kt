package com.example.wallet.domain.model

import com.example.wallet.data.remote.dto.cointicker.CoinTickerDto

data class CoinTicker (
    val id: String,
    val name: String,
    val totalSupply: Int,
    val price: Double = 0.0,
)

fun CoinTickerDto.toDomain() =
    CoinTicker(
        id = id,
        name = name,
        totalSupply = totalSupply,
        price = quotes.usd.price
    )