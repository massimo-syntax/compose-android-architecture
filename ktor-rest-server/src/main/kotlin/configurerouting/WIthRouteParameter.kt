package com.example.configurerouting

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.withRouteParameters(){
    routing {
        // Single path parameter
        get("/todo/{id}") {
            // Extract the 'id' parameter from the path
            val userId = call.parameters["id"]
                ?: return@get call.respondText(
                    "Missing ID",
                    status = HttpStatusCode.BadRequest
                )

            call.respondText("User ID: $userId")
        }

        // Multiple path parameters
        get("/organizations/{orgId}/teams/{teamId}/members") {
            val orgId = call.parameters["orgId"]
            val teamId = call.parameters["teamId"]

            call.respondText("Org: $orgId, Team: $teamId")
        }

        // Optional path parameter with wildcard
        get("/files/{path...}") {
            // Captures everything after /files/
            val path = call.parameters.getAll("path")?.joinToString("/")
            call.respondText("File path: $path")
        }
    }
}