package com.example.routing

import com.example.domain.model.UserAuthRequest
import com.example.domain.service.UserService
import com.example.generateToken
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Routing.authRoutes(userService: UserService) {
    route("/auth") {

        post("/register") {
            val request = call.receive<UserAuthRequest>()
            val user = userService.registerUser(request)
            if (user != null) {
                val tokenResponse = generateToken(user, call.application.environment)
                call.respond(HttpStatusCode.Created, tokenResponse)
            } else {
                call.respond(HttpStatusCode.Conflict, "User already exists")
            }
        }

        post("/login") {
            val request = call.receive<UserAuthRequest>()
            val user = userService.loginUser(request)
            if (user != null) {
                val tokenResponse = generateToken(user, call.application.environment)
                call.respond(HttpStatusCode.OK, tokenResponse)
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
            }
        }
    }
}
