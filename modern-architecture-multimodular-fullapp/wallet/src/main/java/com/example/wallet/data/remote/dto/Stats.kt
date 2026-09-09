package com.example.wallet.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Stats(
    @SerialName("contributors")
    val contributors: Int? = null,
    @SerialName("followers")
    val followers: Int? = null,
    @SerialName("stars")
    val stars: Int? = null,
    @SerialName("subscribers")
    val subscribers: Int? = null
)