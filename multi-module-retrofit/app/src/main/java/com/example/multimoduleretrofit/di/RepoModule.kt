package com.example.multimoduleretrofit.di

import com.example.data.repository.ItemsRepositoryImpl
import com.example.domain.repository.ItemsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    /*
    @Provides
    @Singleton
    fun provideMyRepository(): ItemsRepository {
        return ItemsRepositoryImpl()
    }
    */


    @Binds
    abstract fun bindItemsRepository(
        itemsRepositoryImpl: ItemsRepositoryImpl
    ): ItemsRepository
}
