package com.example

import com.example.auth.authRoutes
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
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
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
            route("/todos") {

                get("/{id}") {
                    val id = call.parameters["id"]?.toLongOrNull()
                        ?: throw ValidationException("Invalid user ID format")

                    val todos = mutableListOf(Todo(0, "My Todo"))
                    val todo = todos.firstOrNull { it.id == id }
                        ?: throw NotFoundException("User with ID $id not found")

                    call.respond(ApiResponse(success = true, data = todo))
                }

                post {
                    // POST - create todo from JSON body
                    createTodo()
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

        // register, login
        authRoutes()

        authenticate("auth-jwt") {
            route("/protected") {
                get("/profile") {
                    val principal = call.principal<JWTPrincipal>()
                    val payload = principal!!.payload
                    val username = payload.getClaim("username").asString()
                    val now = System.currentTimeMillis()
                    val expiresAt = principal.expiresAt?.time?.minus(now)
                    call.respondText("Hello, $username! " + "Token is expired at $expiresAt ms.")
                }
            }
        }
    }


}


suspend fun RoutingContext.createTodo() {
    val request = call.receive<Todo>()
    val newTodo = Todo(0, request.title)
    // insert in db
    call.respond(
        HttpStatusCode.Created,
        ApiResponse(success = true, data = newTodo)
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


