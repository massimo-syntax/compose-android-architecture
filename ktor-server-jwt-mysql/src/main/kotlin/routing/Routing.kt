package com.example.routing

import com.example.JwtService
import com.example.domain.model.TokenResponse
import com.example.domain.model.UserAuthRequest
import com.example.domain.service.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.*
import org.koin.ktor.ext.get

fun Application.configureRouting() {
    val jwtService = get<JwtService>()
    val userService = get<UserService>()

    routing {
        get("/") {
            call.respondText("Hello, World!")
        }
        get("/json/kotlinx-serialization") {
            call.respond(mapOf("hello" to "world"))
        }

        // Register route
        post("/register") {
            val req = call.receive<UserAuthRequest>()
            val user = userService.registerUser(req)
            if (user != null) {
                call.respond(
                    HttpStatusCode.Created,
                    TokenResponse(jwtService.generateToken(user.id.toString()))
                )
            } else {
                call.respond(HttpStatusCode.Conflict, "User already exists")
            }
        }

        // Login route
        post("/login") {
            val req = call.receive<UserAuthRequest>()
            val user = userService.loginUser(req)
            if (user != null) {
                call.respond(
                    HttpStatusCode.OK,
                    TokenResponse(jwtService.generateToken(user.id.toString()))
                )
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
            }
        }

        // Protected user routes
        authenticate("auth-jwt") {
            userRoutes(get())
        }
    }
}