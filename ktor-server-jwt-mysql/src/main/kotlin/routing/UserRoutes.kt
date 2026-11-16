package com.example.routing

import com.example.domain.model.User
import com.example.domain.model.UserAuthRequest
import com.example.domain.repository.UserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Routing.userRoutes(userRepository: UserRepository) {
    route("/users") {
        // Create user
        post {
            val userRequest = call.receive<UserAuthRequest>()
            val user = userRepository.createUser(userRequest)
            if (user != null) {
                call.respond(HttpStatusCode.Created, user.id)
            } else {
                call.respond(HttpStatusCode.Conflict)
            }
        }

        // Read user
        get("/{id}") {
            val id = call.parameters["id"]?.toLong() ?: throw IllegalArgumentException("Invalid ID")
            val user = userRepository.getUserById(id)
            if (user != null) {
                call.respond(HttpStatusCode.OK, user)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        // Update user
        put("/{id}") {
            val id = call.parameters["id"]?.toLong() ?: throw IllegalArgumentException("Invalid ID")
            val user = call.receive<User>()
            userRepository.updateUser(user.copy(id = id))
            call.respond(HttpStatusCode.NoContent)
        }

        // Delete user
        delete("/{id}") {
            val id = call.parameters["id"]?.toLong() ?: throw IllegalArgumentException("Invalid ID")
            userRepository.deleteUser(id)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}
