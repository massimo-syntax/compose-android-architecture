package com.example.hiltretrofit.di.network

import com.example.hiltretrofit.data.remote.DefaultApiService
import com.example.hiltretrofit.data.remote.MockApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {

    @Provides
    fun provideDefaultApiService(
        @DefaultInterceptorOkHttpClient okHttpClient: OkHttpClient
    ): DefaultApiService {
        return Retrofit.Builder()
            .baseUrl("https://example.com")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DefaultApiService::class.java)
    }

    @Provides
    fun provideMockApiService(
        @MockInterceptorOkHttpClient client: OkHttpClient
    ): MockApiService {
        return Retrofit.Builder()
            .baseUrl("https://urlnotneeded.io")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MockApiService::class.java)
    }

}