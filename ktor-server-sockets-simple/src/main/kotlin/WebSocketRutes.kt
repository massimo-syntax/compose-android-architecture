package com.example

import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Message(
    val text: String,
    @SerialName("sender_id")
    val senderId: String,
    @SerialName("receiver_id")
    val receiverId: String,
    @SerialName("connection_id")
    val connectionId: String? = null
)


fun Routing.webSocketRoutes() {
    webSocket("/ws") { // websocketSession
        val firstMessage = receiveDeserialized<Message>()
        // create a connection id if first message of current connection
        val connectionId = firstMessage.connectionId ?: run {
            val newId = UUID.randomUUID().toString()
            ConnectionManager.addConnection(newId, this)
            newId
        }
        // broadcast with connection id
        ConnectionManager.broadcast(message = firstMessage.copy(connectionId = connectionId).toString())

        // all the other messages should already contain the connectionId value
        try {
            while (true) {
                val message = receiveDeserialized<Message>()
                ConnectionManager.broadcast(message = message.toString())
            }
        } finally {
            ConnectionManager.removeConnection(connectionId)
        }
    }

    webSocket("echo") {
        val myMessage = receiveDeserialized<Message>()
        sendSerialized(myMessage)
    }

}