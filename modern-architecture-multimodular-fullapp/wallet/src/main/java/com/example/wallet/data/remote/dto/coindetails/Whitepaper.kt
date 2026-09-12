package com.example.wallet.data.remote.dto.coindetails


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Whitepaper(
    @SerialName("link")
    val link: String = "",
    @SerialName("thumbnail")
    val thumbnail: String = ""
)