package com.example.data.di

import com.example.data_remote.api.ScienceApi
import com.example.data_remote.config.RemoteConfig
import com.example.data_remote.utilFactory.OkHttpFactory
import com.example.data_remote.utilFactory.RetrofitFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(remoteConfig: RemoteConfig): OkHttpClient =
        OkHttpFactory.create(remoteConfig)

    @Provides
    @Singleton
    fun provideRemoteConfig(): RemoteConfig =
        RemoteConfig(
            baseUrl = "https://api.mydummyapi.com" ,
            enableLogging = true,
        )

    @Provides
    @Singleton
    fun provideRetrofit(
        remoteConfig: RemoteConfig,
        moshi: Moshi,
        httpClient: OkHttpClient
    ): Retrofit {
        return RetrofitFactory.create(config = remoteConfig, client = httpClient, moshi = moshi)
    }

    @Provides
    @Singleton
    fun provideMovieApi(retrofit: Retrofit): ScienceApi = retrofit.create(ScienceApi::class.java)

}