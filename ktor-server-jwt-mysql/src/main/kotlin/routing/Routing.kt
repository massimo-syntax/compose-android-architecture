package com.example.routing

import com.example.domain.repository.BlogsRepository
import io.ktor.server.application.*
import io.ktor.server.response.respondText
import io.ktor.server.routing.*
import org.koin.ktor.ext.get

fun Application.configureRouting() {
    val blogsRepository = get<BlogsRepository>()

    routing {
        get("/") {
            call.respondText("Hello, World!")
        }

        blogPublicRoutes(blogsRepository)
        userRoutes(get())
        blogProtectedRoutes(blogsRepository)
    }
}