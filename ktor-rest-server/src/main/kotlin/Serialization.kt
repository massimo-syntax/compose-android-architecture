package com.example

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(Json {
            // Pretty print JSON responses
            prettyPrint = true
            // Don't fail on unknown keys in requests
            ignoreUnknownKeys = true
            // Include default values in serialization
            encodeDefaults = true
        })
    }
}