package com.example.routing

import com.example.domain.repository.BlogsRepository
import com.example.domain.service.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.*
import org.koin.ktor.ext.get

fun Application.configureRouting() {
    val blogsRepository = get<BlogsRepository>()
    val userService = get<UserService>()

    routing {
        get("/") {
            call.respondText("Hello, World!")
        }

        authRoutes(userService)
        blogPublicRoutes(blogsRepository)
        userRoutes(get())
        blogProtectedRoutes(blogsRepository)

        // jwt is the same name given in the Security.kt file configuring JWT
        authenticate("jwt") {
            get("/protected"){
                // get the username for the authenticated user
                val username = call.principal<JWTPrincipal>()?.get("username")
                    ?: throw Exception("username not contained in token principal")
                call.respond(HttpStatusCode.OK, mapOf("username" to username))
            }
        }

    }
}