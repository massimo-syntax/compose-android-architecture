package com.example

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ktor.server.application.Application

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module(){
    configureSerialization()
    configureCallLogging()
    configureRouting()

    // load fcm config file
    val FcmServiceAccount = this::class.java.classLoader.getResourceAsStream("fcm-service-account.json")

    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(FcmServiceAccount))
        .build()

    FirebaseApp.initializeApp(options)

}