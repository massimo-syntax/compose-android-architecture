package com.example.wallet.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Links(
    @SerialName("explorer")
    val explorer: List<String> = listOf(),
    @SerialName("facebook")
    val facebook: List<String> = listOf(),
    @SerialName("reddit")
    val reddit: List<String> = listOf(),
    @SerialName("source_code")
    val sourceCode: List<String> = listOf(),
    @SerialName("website")
    val website: List<String> = listOf(),
    @SerialName("youtube")
    val youtube: List<String> = listOf()
)
