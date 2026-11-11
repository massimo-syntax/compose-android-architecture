package com.example

import com.example.configuration.configureDi
import com.example.configuration.configureGson
import io.ktor.server.application.*
import io.ktor.server.netty.*


fun main(args: Array<String>): Unit = EngineMain.main(args)


fun Application.module() {
    configureGson()
    configureDi()
    configureRouting()
}
