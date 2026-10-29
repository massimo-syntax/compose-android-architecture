package com.example.domain.model.emaillist

data class EmailListItem(
    val id: String,
    val title: String,
    val from: String,
    val image: String,
    val timestamp: Long,
    val favorite: Boolean
)
