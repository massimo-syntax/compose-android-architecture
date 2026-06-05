package com.example.hiltretrofit.di.network

import com.example.hiltretrofit.data.remote.MockServer
import com.example.hiltretrofit.data.remote.interceptors.DefaultInterceptor
import com.example.hiltretrofit.data.remote.interceptors.MockInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Qualifier
import jakarta.inject.Singleton
import okhttp3.OkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultInterceptorOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MockInterceptorOkHttpClient

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideDefaultInterceptor(): DefaultInterceptor{
        return DefaultInterceptor()
    }

    @DefaultInterceptorOkHttpClient
    @Provides
    fun provideOkHttpClientDefault(
        interceptor: DefaultInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    fun provideMockInterceptor(): MockInterceptor{
        return MockInterceptor(MockServer)
    }

    @MockInterceptorOkHttpClient
    @Provides
    fun provideMockInterceptorOkHttpClient(
        mockInterceptor: MockInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(mockInterceptor)
            .build()
    }



}