package com.example

import com.example.model.NotificationDto
import com.example.model.toFcmMessage
import com.google.firebase.messaging.FirebaseMessaging
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello, World!")
        }
        post("/notification"){
            val message = call.receive<NotificationDto>()
            // send the notification !
            FirebaseMessaging.getInstance().send(message.toFcmMessage())
            call.respond(HttpStatusCode.OK)
        }
    }
}