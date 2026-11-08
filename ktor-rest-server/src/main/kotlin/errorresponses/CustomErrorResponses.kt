package com.example.errorresponses

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.application.log
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.ContentTransformationException
import io.ktor.server.response.respond
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val success: Boolean = false,
    val error: String,
    val code: String? = null
)

// Custom exceptions
class NotFoundException(message: String) : Exception(message)
class ValidationException(message: String) : Exception(message)
class UnauthorizedException(message: String) : Exception(message)

fun Application.configureStatusPages() {
    install(StatusPages) {
        // Handle specific exceptions
        exception<NotFoundException> { call, cause ->
            call.respond(
                HttpStatusCode.NotFound,
                ErrorResponse(error = cause.message ?: "Resource not found", code = "NOT_FOUND")
            )
        }

        exception<ValidationException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(error = cause.message ?: "Validation failed", code = "VALIDATION_ERROR")
            )
        }

        exception<UnauthorizedException> { call, cause ->
            call.respond(
                HttpStatusCode.Unauthorized,
                ErrorResponse(error = cause.message ?: "Unauthorized", code = "UNAUTHORIZED")
            )
        }

        // Handle JSON parsing errors
        exception<ContentTransformationException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(error = "Invalid request body", code = "INVALID_JSON")
            )
        }

        // Catch-all for unexpected errors
        exception<Throwable> { call, cause ->
            call.application.log.error("Unhandled exception", cause)
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(error = "Internal server error", code = "INTERNAL_ERROR")
            )
        }

        // Handle HTTP status codes
        status(HttpStatusCode.NotFound) { call, status ->
            call.respond(
                status,
                ErrorResponse(error = "Endpoint not found", code = "ENDPOINT_NOT_FOUND")
            )
        }
    }
}