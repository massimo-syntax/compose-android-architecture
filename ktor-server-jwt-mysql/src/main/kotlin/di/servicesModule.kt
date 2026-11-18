package com.example.di

import com.example.JwtService
import com.example.data.serivce.UserServiceImpl
import com.example.domain.service.UserService
import io.ktor.server.config.ApplicationConfig
import org.koin.dsl.module

val servicesModule = module {

    single<JwtService> {
        // access config
        JwtService(get<ApplicationConfig>())
    }

    single<UserService> {
        UserServiceImpl(get())
    }

}