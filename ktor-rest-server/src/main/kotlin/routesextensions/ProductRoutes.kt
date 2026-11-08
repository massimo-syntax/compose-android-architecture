package com.example.routesextensions

import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.productRoutes() {
    route("/products") {
        get {
            call.respondText("List products")
        }

        get("/{id}") {
            val id = call.parameters["id"]
            call.respondText("Get product $id")
        }
    }
}