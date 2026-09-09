package com.example.wallet.data.remote.dto


import com.example.wallet.domain.model.Coin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinDto(
    @SerialName("id")
    val id: String = "",
    @SerialName("is_active")
    val isActive: Boolean = false,
    @SerialName("is_new")
    val isNew: Boolean = false,
    @SerialName("name")
    val name: String = "",
    @SerialName("rank")
    val rank: Int = 0,
    @SerialName("symbol")
    val symbol: String = "",
    @SerialName("type")
    val type: String = ""
)

fun CoinDto.toDomain(): Coin {
    return Coin(
        id = id,
        isActive = isActive,
        isNew = isNew,
        name = name,
        rank = rank,
        symbol = symbol,
        type = type
    )
}
