package com.example

import com.example.JwtService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.respond
import org.koin.ktor.ext.get

fun Application.configureSecurity() {
    val jwtService = get<JwtService>()

    authentication {
        jwt("auth-jwt") {
            realm = jwtService.realm
            verifier(jwtService.verifier)
            validate { credential ->
                val userId = credential.payload.getClaim("userId").asString()
                if (!userId.isNullOrBlank()) JWTPrincipal(credential.payload) else null
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is invalid or expired")
            }
        }
    }
}