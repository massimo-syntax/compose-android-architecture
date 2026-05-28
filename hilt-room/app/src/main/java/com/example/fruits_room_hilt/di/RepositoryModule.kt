package com.example.fruits_room_hilt.di

import com.example.fruits_room_hilt.data.repository.fruitRepository.FruitRepository
import com.example.fruits_room_hilt.data.repository.fruitRepository.FruitRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


/*
* in the viewmodel the repo with the interface FruitRepository is constructor injected..
* */

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindFruitRepository(impl: FruitRepositoryImpl) : FruitRepository
}