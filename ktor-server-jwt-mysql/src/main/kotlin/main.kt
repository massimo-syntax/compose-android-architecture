package com.example

import com.example.db.initDb
import com.example.di.configureKoin
import com.example.routing.configureRouting
import io.ktor.server.application.Application

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureKoin()
    configureSerialization()
    configureSecurity()
    initDb()
    configureRouting()
}