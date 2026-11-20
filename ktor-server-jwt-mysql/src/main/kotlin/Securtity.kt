package com.example

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.domain.model.TokenResponse
import com.example.domain.model.User
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.response.respond
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.toJavaInstant


fun Application.configureSecurity() {
    // Access to 'enviroment.config' should be above the 'authentication' block
    val jwtConfig = environment.config.config("jwt")
    val jwtAudience = jwtConfig.property("audience").getString()
    val jwtDomain = jwtConfig.property("domain").getString()
    val jwtRealm = jwtConfig.property("realm").getString()
    val jwtSecret = jwtConfig.property("secret").getString()

    authentication {
        // same name to also tag the protected routes
        jwt(name = "jwt") {
            realm = jwtRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtDomain)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience))
                    JWTPrincipal(credential.payload)
                else
                    null
            }
            // token not valid
            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, "Token is invalid or expired")
            }

        }
    }
}

fun generateToken(user: User, applicationEnv: ApplicationEnvironment): TokenResponse {
    val jwtConfig = applicationEnv.config.config("jwt")
    val jwtAudience = jwtConfig.property("audience").getString()
    val jwtDomain = jwtConfig.property("domain").getString()
    val jwtSecret = jwtConfig.property("secret").getString()

    // if password is correct generate a new JWT token
    val expiresAt = Clock.System.now().plus(30.days)
    val token = JWT.create()
        .withAudience(jwtAudience)
        .withIssuer(jwtDomain)
        .withClaim("username", user.name)
        .withExpiresAt(expiresAt.toJavaInstant())
        .sign(Algorithm.HMAC256(jwtSecret))
    return TokenResponse(token = token, expiresAt = expiresAt.toLocalDateTime(TimeZone.UTC).toString() )
}