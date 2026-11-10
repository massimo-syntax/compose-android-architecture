# JWT Authentication Setup

## 1. Dependencies

First, add the required dependencies:

```kotlin
// Authentication
implementation("io.ktor:ktor-server-auth-jvm")
implementation("io.ktor:ktor-server-auth-jwt-jvm")
```

## 2. Add JWT config to `application.yaml`

Create or update `src/main/resources/application.yaml` with the JWT constants:

```yaml
jwt:
  secret: "secret"
  issuer: "http://0.0.0.0:8080/"
  audience: "http://0.0.0.0:8080/profile"
  realm: "Access to 'profile'"
```

## 3. Configure Security

Create the `configureSecurity` Application extension:

```kotlin
package com.example

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.errorresponses.ErrorResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.response.respond

fun Application.configureSecurity() {
    val jwtSecret = environment.config.property("jwt.secret").getString()
    val jwtIssuer = environment.config.property("jwt.issuer").getString()
    val jwtAudience = environment.config.property("jwt.audience").getString()
    val jwtRealm = environment.config.property("jwt.realm").getString()

    install(Authentication) {
        jwt("auth-jwt") {
            realm = jwtRealm

            verifier(
                JWT.require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtIssuer)
                    .build()
            )

            validate { credential ->
                // Extract claims and create principal
                if (credential.payload.audience.contains(jwtAudience)) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            // Authentication failure
            challenge { defaultScheme, realm ->
                call.respond(
                    HttpStatusCode.Unauthorized,
                    ErrorResponse(error = "Token is invalid or expired")
                )
            }
        }
    }
}
```

## 3.5. Register in `application.yaml`

Add `configureSecurity` to the modules list:

```yaml
ktor:
  application:
    modules:
      - com.example.SerializationKt.configureSerialization
      - com.example.errorresponses.CustomErrorResponsesKt.configureStatusPages
      - com.example.SecurityJwtKt.configureSecurity
      - com.example.RoutingKt.configureRouting
```

## 4. Add Bcrypt Dependency

Add the Bcrypt library for password hashing to `build.gradle.kts`:

```kotlin
implementation("org.mindrot:jbcrypt:0.4")
```

## 5. Create User Data Class

Define a `User` data class to represent user credentials:

```kotlin
data class User(
    val userName: String,
    val email: String,
    val password: String
)
```

## 6. Custom Validation

Create validators for email and password, plus a request validation extension:

```kotlin
object Validators {
    fun validateEmail(email: String): Boolean {
        return email.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)$"))
    }

    fun validatePassword(password: String): List<String> {
        val errors = mutableListOf<String>()
        if (password.length < 8) errors.add("Password must be at least 8 characters")
        if (!password.any { it.isUpperCase() }) errors.add("Password must contain uppercase letter")
        if (!password.any { it.isDigit() }) errors.add("Password must contain a digit")
        return errors
    }
}

// Request validation extension
suspend inline fun <reified T> ApplicationCall.receiveValidated(
    block: suspend (T) -> List<String>
): T where T : Any {
    val request = receive<T>()
    val errors = block(request)
    if (errors.isNotEmpty()) {
        // custom exception
        throw ValidationException(errors.joinToString("; "))
    }
    return request
}

// Usage in routes
// post("/users") {
//     val request = call.receiveValidated<CreateUserRequest> { req ->
//         val errors = mutableListOf<String>()
//         if (req.name.isBlank()) errors.add("Name is required")
//         if (!Validators.validateEmail(req.email)) errors.add("Invalid email format")
//         errors.addAll(Validators.validatePassword(req.password))
//         errors
//     }
//     ...
// }
```

## 7. Auth Routes

Create the authentication routes for registration and login. This includes password hashing and token creation:

```kotlin
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
            call.respond(status = HttpStatusCode.Created, message = hashMapOf("success" to true))
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
```

## 8. Protect Routes

Wrap routes with `authenticate("auth-jwt")`:

```kotlin
authenticate("auth-jwt") {
    route("/protected") {
        get("/profile") {
            val principal = call.principal<JWTPrincipal>()
            val username = principal!!.payload.getClaim("username").asString()
            call.respondText("Hello, $username!")
        }
    }
}
```

Make sure `application.yaml` audience matches the protected route:

```yaml
jwt:
  secret: "secret"
  issuer: "http://0.0.0.0:8080/"
  audience: "http://0.0.0.0:8080/protected"
  realm: "Access to protected routes"
```

## 9. Send the Token

Include the token in the `Authorization` header:

```
Authorization: Bearer <your-jwt>
```
