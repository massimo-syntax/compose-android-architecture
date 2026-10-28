package com.example.data.dto.emaildetails


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmailDetailsDto(
    @SerialName("body")
    val body: String?,
    @SerialName("id")
    val id: String?,
    @SerialName("image")
    val image: String?,
    @SerialName("payload")
    val payload: Payload?,
    @SerialName("title")
    val title: String?
)