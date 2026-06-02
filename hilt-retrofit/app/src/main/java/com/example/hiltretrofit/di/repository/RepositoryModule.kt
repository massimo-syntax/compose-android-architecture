package com.example.hiltretrofit.di.repository

import com.example.hiltretrofit.data.repository.NetworkRepository
import com.example.hiltretrofit.data.repository.NetworkRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

// Source - https://stackoverflow.com/a/64820556
// Posted by Andrew
// Retrieved 2026-06-01, License - CC BY-SA 4.0

@Module
@InstallIn(SingletonComponent::class) // or whatever graph fits your need the best
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindRepository(repository: NetworkRepositoryImpl): NetworkRepository
}
