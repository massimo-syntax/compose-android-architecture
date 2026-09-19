package com.example.weather.di

import com.example.utils.constants.Constants
import com.example.weather.data.remote.WeatherAPI
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WeatherModule {


    @Provides
    @Singleton
    fun provideWeatherApi(): WeatherAPI {
        val httpClient = OkHttpClient().newBuilder()
        // logging interceptor
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        // my interceptor
        val myInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder().build()
            chain.proceed(request)
        }

        httpClient.addInterceptor(httpLoggingInterceptor)
        httpClient.addInterceptor(httpLoggingInterceptor)

        val json = Json {
            ignoreUnknownKeys = true
            // when 'null' field serialize to empty array
            coerceInputValues = true
        }

        val converterFactory = json.asConverterFactory(
            "application/json; charset=utf-8".toMediaType())

        return Retrofit.Builder()
            .baseUrl(Constants.WEATHER_BASE_URL)
            .client(httpClient.build())
            .addConverterFactory(converterFactory)
            .build()
            .create(WeatherAPI::class.java)
    }
}