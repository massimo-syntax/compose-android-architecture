package com.example.data.di

import android.content.Context
import com.example.data_local.DatabaseFactory
import com.example.data_local.ScienceDatabase
import com.example.data_local.dao.ScienceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): ScienceDatabase {
        return DatabaseFactory.create(context)
    }

    @Provides
    fun provideMovieDao(
        database: ScienceDatabase
    ): ScienceDao {
        return database.scienceDao()
    }
}