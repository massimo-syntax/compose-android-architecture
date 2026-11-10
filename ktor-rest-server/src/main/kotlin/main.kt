package com.example

import com.example.errorresponses.configureStatusPages
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

// Application module - this is where you configure plugins and routes
fun Application.module() {
    configureSerialization()
    //configureMonitoring()
    configureStatusPages()
    configureSecurity()
    configureRouting()
}
