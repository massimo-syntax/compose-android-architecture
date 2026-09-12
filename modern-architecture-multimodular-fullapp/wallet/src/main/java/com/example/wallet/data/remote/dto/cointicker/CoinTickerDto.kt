package com.example.wallet.data.remote.dto.cointicker


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinTickerDto(
    @SerialName("beta_value")
    val betaValue: Double = 0.0,
    @SerialName("first_data_at")
    val firstDataAt: String = "",
    @SerialName("id")
    val id: String = "",
    @SerialName("last_updated")
    val lastUpdated: String = "",
    @SerialName("max_supply")
    val maxSupply: Int = 0,
    @SerialName("name")
    val name: String = "",
    @SerialName("quotes")
    val quotes: Quotes = Quotes(),
    @SerialName("rank")
    val rank: Int = 0,
    @SerialName("symbol")
    val symbol: String = "",
    @SerialName("total_supply")
    val totalSupply: Int = 0
)