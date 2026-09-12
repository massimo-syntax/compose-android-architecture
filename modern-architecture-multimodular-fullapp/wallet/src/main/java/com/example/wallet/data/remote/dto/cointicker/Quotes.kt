package com.example.wallet.data.remote.dto.cointicker


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Quotes(
    @SerialName("USD")
    val usd: USD = USD()
)