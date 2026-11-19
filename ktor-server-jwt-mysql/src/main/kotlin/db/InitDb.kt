package com.example.db

import com.example.db.tables.BlogsTable
import com.example.db.tables.UsersTable
import io.ktor.server.application.Application
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.SchemaUtils
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction


fun Application.initDb() {
    val url = environment.config.property("database.url").getString()
    val user = environment.config.property("database.user").getString()
    val driver = environment.config.property("database.driver").getString()
    val password = "password" // System.getenv("db_password_ktor112233")

    val db = R2dbcDatabase.connect(
        url = url,
        user = user,
        driver = driver,
        password = password
    )

    runBlocking {
        suspendTransaction(db) {
            SchemaUtils.create(UsersTable, BlogsTable)
        }
    }

}