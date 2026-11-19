package com.example.di

import com.example.data.serivce.UserServiceImpl
import com.example.domain.service.UserService
import org.koin.dsl.module

val servicesModule = module {
    single<UserService> {
        UserServiceImpl(get())
    }
}