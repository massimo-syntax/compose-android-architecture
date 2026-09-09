package com.example.wallet.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    @SerialName("coin_counter")
    val coinCounter: Int = 0,
    @SerialName("ico_counter")
    val icoCounter: Int = 0,
    @SerialName("id")
    val id: String = "",
    @SerialName("name")
    val name: String = ""
)
