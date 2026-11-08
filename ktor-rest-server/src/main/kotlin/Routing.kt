package com.example

import com.example.configurerouting.crudRoutes
import com.example.configurerouting.queryParameters
import com.example.configurerouting.withRouteParameters
import com.example.errorresponses.NotFoundException
import com.example.errorresponses.ValidationException
import com.example.model.Todo
import com.example.reqres.ApiResponse
import com.example.reqres.CreateUserRequest
import com.example.reqres.User
import com.example.routesextensions.productRoutes
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello, Ktor!")
        }
        // ping
        get("/health") {
            call.respondText("OK")
        }
        // more modular by Application. scope
        crudRoutes()
        withRouteParameters()
        queryParameters()

        //Grouping Routes with route()
        // API version prefix
        route("/api/v2") {
            // User routes
            route("/users") {

                get("/{id}") {
                    val id = call.parameters["id"]?.toLongOrNull()
                        ?: throw ValidationException("Invalid user ID format")

                    val todos = mutableListOf<Todo>()
                    val todo = todos.firstOrNull { it.id == id }
                        ?: throw NotFoundException("User with ID $id not found")

                    call.respond(ApiResponse(success = true, data = todo))
                }

                post {
                    // POST - create user from JSON body
                    post {
                        createUser(call)
                    }
                }
                route("/{id}") {
                    get { /* get user by id */ }
                    put { /* update user */ }
                    delete { /* delete user */ }
                    // Nested resources
                    route("/orders") {
                        get { /* get user's orders */ }
                    }
                }
            }
        }

        // Modular Route by Route. extensions
        route("/api/v1") {
            // Include route modules
            productRoutes()

        }


    }
}

suspend fun ApplicationCall.createTodo() {
    val request = receive<Todo>()
    val newUser = Todo(0, request.title)
    // insert in db
    respond(
        HttpStatusCode.Created,
        ApiResponse(success = true, data = newUser)
    )

}

private suspend fun createUser(call: ApplicationCall) {
    // Receive and deserialize JSON body
    val request = call.receive<CreateUserRequest>()

    // Create user (simulate database insert)
    val newUser = User(
        id = 1,
        name = request.name,
        email = request.email
    )

    call.respond(
        HttpStatusCode.Created,
        ApiResponse(success = true, data = newUser)
    )

}


