package com.example.configurerouting

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.queryParameters(){
    routing {
        get("/todos") {
            // Extract query parameters with defaults
            val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
            val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10
            val sortBy = call.request.queryParameters["sortBy"] ?: "createdAt"
            val order = call.request.queryParameters["order"] ?: "desc"

            // Multiple values for the same parameter (e.g., ?status=active&status=pending)
            val statuses = call.request.queryParameters.getAll("status") ?: emptyList()

            call.respondText(
                "Page: $page, Limit: $limit, Sort: $sortBy $order, Statuses: $statuses"
            )
        }
    }
}