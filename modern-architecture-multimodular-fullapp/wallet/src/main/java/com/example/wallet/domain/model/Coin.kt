package com.example.wallet.domain.model

data class Coin(
    val id: String = "",
    val isActive: Boolean = false,
    val isNew: Boolean = false,
    val name: String = "",
    val rank: Int = 0,
    val symbol: String = "",
    val type: String = ""
)
