package com.example.wallet.data.remote.dto


import com.example.wallet.domain.model.CoinDetail
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class CoinDetailDto(
    @SerialName("description")
    val description: String = "",
    @SerialName("development_status")
    val developmentStatus: String = "",
    @SerialName("first_data_at")
    val firstDataAt: String = "",
    @SerialName("hardware_wallet")
    val hardwareWallet: Boolean = false,
    @SerialName("hash_algorithm")
    val hashAlgorithm: String = "",
    @SerialName("id")
    val id: String = "",
    @SerialName("is_active")
    val isActive: Boolean = false,
    @SerialName("is_new")
    val isNew: Boolean = false,
    @SerialName("last_data_at")
    val lastDataAt: String = "",
    @Transient
    @SerialName("links")
    val links: Links = Links(),
    @SerialName("links_extended")
    @Transient
    val linksExtended: List<LinksExtended> = listOf(),
    @SerialName("logo")
    val logo: String = "",
    @SerialName("message")
    val message: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("open_source")
    val openSource: Boolean = false,
    @SerialName("org_structure")
    val orgStructure: String = "",
    @SerialName("proof_type")
    val proofType: String = "",
    @SerialName("rank")
    val rank: Int = 0,
    @SerialName("started_at")
    val startedAt: String = "",
    @SerialName("symbol")
    val symbol: String = "",
    @SerialName("tags")
    val tags: List<Tag> = listOf(),
    @Transient
    @SerialName("team")
    val team: List<Team> = listOf(),
    @SerialName("type")
    val type: String = "",
    @Transient
    @SerialName("whitepaper")
    val whitepaper: Whitepaper = Whitepaper()
)

fun CoinDetailDto.toDomain(): CoinDetail {
    return CoinDetail(
        id = id,
        name = name,
        logo = logo,
        description = description,
        symbol = symbol,
        rank = rank,
        isActive = isActive,
        tags = tags,
        links = links
    )
}
