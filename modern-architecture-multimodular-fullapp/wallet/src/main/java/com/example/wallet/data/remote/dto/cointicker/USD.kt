package com.example.wallet.data.remote.dto.cointicker


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class USD(
    @SerialName("ath_date")
    val athDate: String = "",
    @SerialName("ath_price")
    val athPrice: Double = 0.0,
    @SerialName("market_cap")
    val marketCap: Long = 0,
    @SerialName("market_cap_change_24h")
    val marketCapChange24h: Double = 0.0,
    @SerialName("percent_change_12h")
    val percentChange12h: Double = 0.0,
    @SerialName("percent_change_15m")
    val percentChange15m: Double = 0.0,
    @SerialName("percent_change_1h")
    val percentChange1h: Double = 0.0,
    @SerialName("percent_change_1y")
    val percentChange1y: Int = 0,
    @SerialName("percent_change_24h")
    val percentChange24h: Double = 0.0,
    @SerialName("percent_change_30d")
    val percentChange30d: Int = 0,
    @SerialName("percent_change_30m")
    val percentChange30m: Double = 0.0,
    @SerialName("percent_change_6h")
    val percentChange6h: Double = 0.0,
    @SerialName("percent_change_7d")
    val percentChange7d: Double = 0.0,
    @SerialName("percent_from_price_ath")
    val percentFromPriceAth: Double = 0.0,
    @SerialName("price")
    val price: Double = 0.0,
    @SerialName("volume_24h")
    val volume24h: Double = 0.0,
    @SerialName("volume_24h_change_24h")
    val volume24hChange24h: Double = 0.0
)