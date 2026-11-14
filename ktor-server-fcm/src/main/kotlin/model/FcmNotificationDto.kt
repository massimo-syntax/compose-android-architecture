package com.example.model

import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import kotlinx.serialization.Serializable

@Serializable
data class NotificationDto(
    val to: String,
    val notification: NotificationBody
)
@Serializable
data class NotificationBody(
    val title: String,
    val body: String
)

fun NotificationDto.toFcmMessage() = Message
    .builder()
    .setNotification(
        Notification.builder()
            .setTitle(notification.title)
            .setBody(notification.body)
            .build()
    ).setToken(to)
    .build()
