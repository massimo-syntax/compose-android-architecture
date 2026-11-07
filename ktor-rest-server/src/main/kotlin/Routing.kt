package com.example

import com.example.model.Data
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.engine.EngineConnectorBuilder
import io.ktor.server.engine.embeddedServer
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.Netty
import io.ktor.server.request.receive
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlin.collections.emptyList

fun Application.configureRouting() {

    val data = mutableListOf(
        Data(0, "hello"),
        Data(1, "hello1"),
        Data(2, "hello2"),
        Data(3, "hello3"),
        Data(4, "hello4"),
    )

    routing {
        staticResources("/static", "static")

        get("/") {
            call.respondText("Hello, World!")
        }

        get("/data") {
            call.respond(data)
        }

        route("/data"){

            get("/contains/{str}") {
                val param = call.parameters["str"]
                call.respond {
                    if (param != null) {
                        data.filter { it.name.contains(param) }
                    } else {
                        emptyList()
                    }
                }
            }

            get("/id/{id}"){
                val id = call.parameters["id"]?.toInt()
                if(id == null){
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }

                val data = data.firstOrNull { it.id == id }
                if(data == null){
                    //call.respond(HttpStatusCode.NotFound)
                    call.respond(emptyList<Data>())
                    return@get
                }

                call.respond(data)
            }


            post{
                try{
                    val dt = call.receive<Data>()
                    data.add(dt)
                    call.respond(HttpStatusCode.Created)
                } catch (e: Exception) {
                    if (e is CancellationException) {
                        currentCoroutineContext().ensureActive()
                    }
                    call.respond(e.message ?: "Something went wrong")
                }
            }

        }

    }


}