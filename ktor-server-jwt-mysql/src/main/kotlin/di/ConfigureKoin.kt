package com.example.di

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.config.ApplicationConfig
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import org.koin.dsl.module

fun Application.configureKoin(){
    install(Koin){
        slf4jLogger()
        modules(
            listOf(
                // access config, jwt needs that
                module { single<ApplicationConfig> { environment.config } },
                repositoryModule,
                servicesModule
            )
        )
    }
}