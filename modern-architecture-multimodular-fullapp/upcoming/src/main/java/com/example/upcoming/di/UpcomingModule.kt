package com.example.upcoming.di

import com.example.upcoming.data.repository.UpcomingRepositoryImpl
import com.example.upcoming.domain.repository.UpcomingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UpcomingModule {
    @Binds
    @Singleton
    abstract fun bindUpcomingRepository(
        upcomingRepositoryImpl: UpcomingRepositoryImpl
    ): UpcomingRepository
}
