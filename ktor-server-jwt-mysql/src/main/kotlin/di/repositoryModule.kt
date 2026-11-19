package com.example.di

import com.example.data.repository.BlogsRepositoryImpl
import com.example.data.repository.UserRepositoryImpl
import com.example.domain.repository.BlogsRepository
import com.example.domain.repository.UserRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<UserRepository> { UserRepositoryImpl() }
    single<BlogsRepository> { BlogsRepositoryImpl() }

}