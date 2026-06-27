package com.example.decentfolderstructure.di

import com.example.data.api.TodoApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    //https://jsonplaceholder.typicode.com/todos
    @Provides
    @Singleton
    fun provideRetrofit(): TodoApi = Retrofit
        .Builder()
        .baseUrl("https://jsonplaceholder.typicode.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TodoApi::class.java)
}