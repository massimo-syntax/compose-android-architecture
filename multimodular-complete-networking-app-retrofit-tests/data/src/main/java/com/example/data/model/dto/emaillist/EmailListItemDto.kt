package com.example.data.model.dto.emaillist


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmailListItemDto(
    @SerialName("id")
    val id: String?,
    @SerialName("title")
    val title: String?,
    @SerialName("from")
    val from: String?,
    @SerialName("image")
    val image: String?,
    @SerialName("timestamp")
    val timestamp: Long?,
    @SerialName("favorite")
    val favorite: Boolean?
)