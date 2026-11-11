package com.example.configuration

import com.example.domain.ports.FitnessRepository
import com.example.infrastructiure.repository.FitnessRepositoryImpl
import com.mongodb.kotlin.client.coroutine.MongoClient
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureDi(){
    install(Koin) {
        slf4jLogger()
        modules(
            module {
            single { MongoClient.create(
                environment.config.propertyOrNull("ktor.mongo.uri")?.getString() ?: throw RuntimeException("Failed to access MongoDB URI.")
            ) }
            single { get<MongoClient>().getDatabase(environment.config.property("ktor.mongo.database").getString()) }
        },
            module {
            single<FitnessRepository> { FitnessRepositoryImpl(get()) }
        })
    }
}