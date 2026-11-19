package com.example.routing

import com.example.domain.model.BlogRequest
import com.example.domain.repository.BlogsRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post

fun Routing.blogPublicRoutes(blogsRepository: BlogsRepository) {
    get("/blogs/all") {
        val blogs = blogsRepository.getAll()
        call.respond(HttpStatusCode.OK, blogs)
    }
}

fun Routing.blogProtectedRoutes(blogsRepository: BlogsRepository) {
    post("/blogs") {
        val request = call.receive<BlogRequest>()
        val blog = blogsRepository.createBlog(1, request)
        call.respond(HttpStatusCode.Created, blog)
    }
}
