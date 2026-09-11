package com.example.wallet.di

import com.example.utils.constants.Constants
import com.example.wallet.data.remote.CoinApi
import com.example.wallet.data.repository.CoinRepositoryImpl
import com.example.wallet.domain.repository.CoinRepository
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
object WalletModule {
    @Provides
    @Singleton
    fun provideCoinPaprikaApi(): CoinApi {
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
            .baseUrl(Constants.BASE_URL_COINS)
            .client(httpClient.build())
            .addConverterFactory(converterFactory)
            .build()
            .create(CoinApi::class.java)
    }

}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule{
    @Binds
    abstract fun bindCoinRepository(impl: CoinRepositoryImpl) : CoinRepository
}