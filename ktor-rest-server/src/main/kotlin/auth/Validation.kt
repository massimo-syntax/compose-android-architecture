package com.example.auth

import com.example.errorresponses.ValidationException
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive

// Validation utilities
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
//post("/users") {
//    val request = call.receiveValidated<CreateUserRequest> { req ->
//        val errors = mutableListOf<String>()
//        if (req.name.isBlank()) errors.add("Name is required")
//        if (!Validators.validateEmail(req.email)) errors.add("Invalid email format")
//        errors.addAll(Validators.validatePassword(req.password))
//        errors
//    }
//    ...
//}