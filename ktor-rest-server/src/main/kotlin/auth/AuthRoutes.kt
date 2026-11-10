package com.example.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.errorresponses.ErrorResponse
import com.example.model.User
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.mindrot.jbcrypt.BCrypt
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.hours

private val users = mutableListOf<User>()

fun Route.authRoutes() {
    route("/register") {
        post {
            val user = call.receive<User>()
            // check for errors
            val errors = mutableListOf<String>()
            if (user.userName.isBlank()) errors.add("Name is required")
            if (!Validators.validateEmail(user.email)) errors.add("Invalid email format")
            errors.addAll(Validators.validatePassword(user.password))
            if (errors.isNotEmpty()) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorResponse(error = errors.joinToString("; "), code = "VALIDATION_ERROR")
                ) // or throw ValidationException(errors.joinToString("; "))

            }
            // insert user in database
            val hash = BCrypt.hashpw(user.password, BCrypt.gensalt())
            // fake database
            val hashedUser = user.copy(password = hash)
            users.add(hashedUser)
            // response
            call.respond(status = HttpStatusCode.Created, message = hashMapOf("sucess" to true))
        }
    }

    route("/login") {
        post {
            val user = call.receive<User>()
            val config = environment.config
            val secret = config.property("jwt.secret").getString()

            val issuer = config.property("jwt.issuer").getString()
            val audience = config.property("jwt.audience").getString()

            // Check username and password
            val stored = users.find { it.userName == user.userName }
            if (stored == null || !BCrypt.checkpw(user.password, stored.password)) {
                call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
            }

            val expiresAt = System.currentTimeMillis() + 1.hours.inWholeMilliseconds
            val token = JWT.create()
                .withAudience(audience)
                .withIssuer(issuer)
                .withClaim("username", user.userName)
                .withClaim("email", user.userName)
                .withExpiresAt(Date(expiresAt))
                .sign(Algorithm.HMAC256(secret))

            call.respond(hashMapOf("token" to token))
        }
    }
}