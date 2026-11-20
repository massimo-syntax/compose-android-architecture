# JWT Setup Guide

## 1. Gradle Dependency

```kotlin
implementation("io.ktor:ktor-server-auth-jwt-jvm")
```

## 2. `application.yaml` Config

```yaml
jwt:
  domain: "http://localhost/"
  audience: "audience-admin"
  realm: "My Realm"
  secret: "your-secret"
```

## 3. `Security.kt` File

*(Note: file is named `Securtity.kt` in the project — typo.)*

```kotlin
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
    val jwtConfig = environment.config.config("jwt")
    val jwtAudience = jwtConfig.property("audience").getString()
    val jwtDomain = jwtConfig.property("domain").getString()
    val jwtRealm = jwtConfig.property("realm").getString()
    val jwtSecret = jwtConfig.property("secret").getString()

    authentication {
        jwt(name = "jwt") {
            realm = jwtRealm
            verifier(
                JWT.require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtDomain)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience))
                    JWTPrincipal(credential.payload)
                else null
            }
            challenge { _, _ ->
                call.respond(
                    HttpStatusCode.Unauthorized,
                    "Token is invalid or expired"
                )
            }
        }
    }
}

fun generateToken(user: User, env: ApplicationEnvironment): TokenResponse {
    val jwtConfig = env.config.config("jwt")
    val jwtSecret = jwtConfig.property("secret").getString()
    val jwtAudience = jwtConfig.property("audience").getString()
    val jwtDomain = jwtConfig.property("domain").getString()

    val expiresAt = Clock.System.now().plus(30.days)
    val token = JWT.create()
        .withAudience(jwtAudience)
        .withIssuer(jwtDomain)
        .withClaim("username", user.name)
        .withExpiresAt(expiresAt.toJavaInstant())
        .sign(Algorithm.HMAC256(jwtSecret))

    return TokenResponse(
        token,
        expiresAt.toLocalDateTime(TimeZone.UTC).toString()
    )
}
```

Call `configureSecurity()` in `main.kt`'s `module()`.

## 4. Login Route — Generate Token

```kotlin
post("/login") {
    val request = call.receive<UserAuthRequest>()
    val user = userService.loginUser(request)
    if (user != null) {
        val response = generateToken(user, call.application.environment)
        call.respond(HttpStatusCode.OK, response)
    } else {
        call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
    }
}
```

Returns `TokenResponse(token, expires_at)`.

## 5. Protected Route

```kotlin
// "jwt" matches the name in Security.kt
authenticate("jwt") {
    get("/protected") {
        val username = call.principal<JWTPrincipal>()?.get("username")
            ?: throw Exception("Username not contained in token principal")
        call.respond(HttpStatusCode.OK, mapOf("username" to username))
    }
}
```
