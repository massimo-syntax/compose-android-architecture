package com.example.configurerouting

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.head
import io.ktor.server.routing.options
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.routing

fun Application.crudRoutes() {
    routing {
        // GET - Retrieve resources
        get("/items") {
            call.respondText("List of items")
        }

        // POST - Create new resources
        post("/items") {
            call.respondText("Item created", status = HttpStatusCode.Created)
        }

        // PUT - Replace entire resource
        put("/items/{id}") {
            val id = call.parameters["id"]
            call.respondText("Item $id replaced")
        }

        // PATCH - Partial update
        patch("/items/{id}") {
            val id = call.parameters["id"]
            call.respondText("Item $id updated")
        }

        // DELETE - Remove resource
        delete("/items/{id}") {
            val id = call.parameters["id"]
            call.respondText("Item $id deleted", status = HttpStatusCode.NoContent)
        }

        // HEAD - Same as GET but without body
        head("/items") {
            call.response.status(HttpStatusCode.OK)
        }

        // OPTIONS - Describe communication options
        options("/items") {
            call.response.header("Allow", "GET, POST, PUT, DELETE")
            call.respond(HttpStatusCode.OK)
        }
    }
}