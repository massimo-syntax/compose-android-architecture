package com.example.domain.model.emaildetails

data class EmailDetails(
    val id: String,
    val body: String,
    val image: String,
    val payload: Payload?,
    val title: String
)
