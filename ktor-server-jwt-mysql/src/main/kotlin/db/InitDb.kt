package com.example.db

import io.ktor.server.application.Application
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase

fun Application.initDb() {
    val url = environment.config.property("database.url").getString()
    val user = environment.config.property("database.user").getString()
    val driver = environment.config.property("database.driver").getString()
    val password = "password"//System.getenv("my_db_112233_password")

    val db = R2dbcDatabase.connect(
        url = url,
        user = user,
        driver = driver,
        password = password
    )
}