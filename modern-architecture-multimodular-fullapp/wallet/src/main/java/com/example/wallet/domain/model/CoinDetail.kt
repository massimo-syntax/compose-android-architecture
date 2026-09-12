package com.example.wallet.domain.model

import com.example.wallet.data.remote.dto.coindetails.Links
import com.example.wallet.data.remote.dto.coindetails.Tag

data class CoinDetail(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val logo: String = "",
    val symbol: String = "",
    val rank: Int = 0,
    val isActive: Boolean = false,
    val tags: List<Tag> = listOf(),
    val links: Links = Links()
)
