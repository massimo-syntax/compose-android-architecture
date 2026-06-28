package com.example.data.di

import com.example.data.repository.ScienceRepositoryImpl
import com.example.domain.repository.ScienceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindMovieRepository(impl: ScienceRepositoryImpl): ScienceRepository
}