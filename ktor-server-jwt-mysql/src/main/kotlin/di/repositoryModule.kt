package com.example.di

import com.example.data.repository.UserRepositoryImpl
import com.example.domain.repository.UserRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<UserRepository> { UserRepositoryImpl() }

}