package com.example

import io.ktor.websocket.DefaultWebSocketSession
import io.ktor.websocket.Frame
import kotlinx.coroutines.isActive
import java.util.concurrent.ConcurrentHashMap

object ConnectionManager {
    private val connections = ConcurrentHashMap<String, DefaultWebSocketSession>()

    fun addConnection(id: String, session: DefaultWebSocketSession) {
        connections[id] = session
    }

    fun removeConnection(id: String) {
        connections.remove(id)
    }

    suspend fun broadcast(message: String, senderId: String? = null) {
        connections.values.forEach { session ->
            if (session.isActive) {
                session.send(Frame.Text(message))
            }
        }
    }
}