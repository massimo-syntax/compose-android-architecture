package com.example

import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)

    // Create and start the Netty server on port 8080
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        // Configure the application module
        module()
    }.start(wait = true)

}

// Application module - this is where you configure plugins and routes
fun Application.module() {
    configureRouting()
}
