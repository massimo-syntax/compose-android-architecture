package com.example.mock_interceptor_retrofit.di

import com.example.mock_interceptor_retrofit.remote.retrofilt.MockApiService
import com.example.mock_interceptor_retrofit.remote.retrofilt.MockInterceptor
import com.example.mock_interceptor_retrofit.remote.retrofilt.MyMockServer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideMockApiService(
        // Potential dependencies of this type
    ): MockApiService {
        val client = OkHttpClient.Builder()
            .addInterceptor(MockInterceptor(MyMockServer))
            .build()
        return Retrofit.Builder()
            .baseUrl("https://fakunusedurlofmockapi.api/") // dummy URL
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MockApiService::class.java)
    }
}