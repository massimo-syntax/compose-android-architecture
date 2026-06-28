package com.example.data_remote.config

data class RemoteConfig(
    val baseUrl: String,
    val apiKey: String? = null,
    val enableLogging: Boolean,
    val connectTimeoutSeconds: Long = 30,
    val readTimeoutSeconds: Long = 30
)