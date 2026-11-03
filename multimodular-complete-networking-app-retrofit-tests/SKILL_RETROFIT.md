# Modern Retrofit Architecture Guide

A clean, modular Android networking guide using **Retrofit 2**, **Hilt**, **Kotlinx Serialization**, and a lightweight **`NetworkResult<T>`** pattern.

---

## 1. Dependencies Setup (`gradle/libs.versions.toml`)

Add the required dependencies and plugins:

```toml
[libraries]
# Retrofit & OkHttp
retrofit = { group = "com.squareup.retrofit2", name = "retrofit", version.ref = "retrofit" }
logging-interceptor = { module = "com.squareup.okhttp3:logging-interceptor", version.ref = "loggingInterceptor" }

# Kotlinx Serialization
kotlinx-serialization-json = { module = "org.jetbrains.kotlinx:kotlinx-serialization-json", version.ref = "kotlinxSerializationJson" }
retrofit2-kotlinx-serialization-converter = { module = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter", version.ref = "retrofit2KotlinxSerializationConverter" }

# Hilt
hilt-android = { module = "com.google.dagger:hilt-android", version.ref = "hiltAndroid" }
hilt-android-compiler = { module = "com.google.dagger:hilt-android-compiler", version.ref = "hiltAndroidCompiler" }

[plugins]
hilt-android = { id = "com.google.dagger.hilt.android", version.ref = "hiltAndroid" }
kotlin-ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }
kotlin-serialization = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlin" }
```

> **Note:** Apply plugins in the root `build.gradle.kts` with `apply false` and in target modules with `alias(...)`.

---

## 2. API Service Interface (`:data` module)

Define network endpoints returning raw DTOs or lists:

```kotlin
package com.example.data.remote.api

import com.example.data.model.dto.DataDetailsDto
import com.example.data.model.dto.DataItemDto
import retrofit2.http.GET

interface ApiService {
    @GET("api/v1/items")
    suspend fun getDataList(): List<DataItemDto>

    @GET("api/v1/details")
    suspend fun getDataDetails(): List<DataDetailsDto>
}
```

---

## 3. Hilt Network Module (`:data` module)

Provide OkHttpClient, Retrofit, and ApiService instances:

```kotlin
package com.example.data.di

import com.example.data.remote.api.ApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val networkJson = Json { ignoreUnknownKeys = true }

    @Provides
    fun provideBaseUrl(): String = "https://example.mockapi.io"

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun provideRetrofit(
        url: String,
        loggingInterceptor: HttpLoggingInterceptor
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
            .client(OkHttpClient.Builder().addInterceptor(loggingInterceptor).build())
            .build()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)
}
```

---

## 4. Network Result & Error Models (`:core` module)

Place `NetworkResult<T>` and `NetworkError` in `:core` (`com.example.core.networkresult`) so `:domain`, `:data`, and UI layers can access them directly without circular dependencies:

```kotlin
package com.example.core.networkresult

sealed interface NetworkError {
    val isRetryable: Boolean get() = false

    data object NoInternet : NetworkError { override val isRetryable = true }
    data object Timeout : NetworkError { override val isRetryable = true }
    data object Server : NetworkError { override val isRetryable = true }

    data object Unauthorized : NetworkError
    data object Forbidden : NetworkError
    data object NotFound : NetworkError
    data object Serialization : NetworkError

    data class Http(val code: Int, val message: String?) : NetworkError {
        override val isRetryable: Boolean get() = code == 408 || code == 429
    }

    data class Unknown(val cause: Throwable) : NetworkError
}

sealed interface NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>
    data class Failure(val error: NetworkError) : NetworkResult<Nothing>
}
```

---

## 5. Safe API Execution & Error Handler (`:data` module)

Encapsulate exception catching and Retrofit `HttpException` / `IOException` mapping in `:data` (`com.example.data.remote.safecall`):

```kotlin
package com.example.data.remote.safecall

import com.example.core.networkresult.NetworkError
import com.example.core.networkresult.NetworkResult
import java.io.IOException
import java.net.SocketTimeoutException
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import retrofit2.HttpException

suspend fun <T> safeApiCall(block: suspend () -> T): NetworkResult<T> =
    try {
        NetworkResult.Success(block())
    } catch (e: CancellationException) {
        throw e // Never swallow Coroutine CancellationException
    } catch (e: Throwable) {
        NetworkResult.Failure(e.toNetworkError())
    }

fun Throwable.toNetworkError(): NetworkError = when (this) {
    is HttpException -> when (code()) {
        401 -> NetworkError.Unauthorized
        403 -> NetworkError.Forbidden
        404 -> NetworkError.NotFound
        in 500..599 -> NetworkError.Server
        else -> NetworkError.Http(code(), message())
    }
    is SocketTimeoutException -> NetworkError.Timeout
    is IOException -> NetworkError.NoInternet
    is SerializationException, // Kotlinx Serialization
    // is JsonDataException,    // Moshi (if used)
    // is JsonParseException    // Gson (if used)
    -> NetworkError.Serialization
    else -> NetworkError.Unknown(this)
}
```

---

## 6. DTOs, Domain Models, and Extension Mappers

### Domain Models (`:domain` module)

```kotlin
package com.example.domain.model

data class DataItem(
    val id: String,
    val title: String,
    val description: String
)
```

### DTOs & Extension Mappers (`:data` module)

Define DTOs with `@Serializable` and place extension mapping functions directly in the DTO or mapper files in `:data`:

```kotlin
package com.example.data.model.dto

import com.example.domain.model.DataItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DataItemDto(
    @SerialName("id") val id: String?,
    @SerialName("title") val title: String?,
    @SerialName("desc") val desc: String?
)

fun DataItemDto.toDomain(): DataItem = DataItem(
    id = id ?: "",
    title = title.orEmpty(),
    description = desc.orEmpty()
)

fun List<DataItemDto>.toDomain(): List<DataItem> = map { it.toDomain() }
```

---

## 7. Repository Interface & Implementation

### Repository Interface (`:domain` module)

```kotlin
package com.example.domain.repository

import com.example.core.networkresult.NetworkResult
import com.example.domain.model.DataItem

interface DataRepository {
    suspend fun getDataList(): NetworkResult<List<DataItem>>
}
```

### Repository Implementation (`:data` module)

Execute the network request cleanly using `safeApiCall` and `.toDomain()`:

```kotlin
package com.example.data.repository

import com.example.core.networkresult.NetworkResult
import com.example.data.model.dto.toDomain
import com.example.data.remote.api.ApiService
import com.example.data.remote.safecall.safeApiCall
import com.example.domain.model.DataItem
import com.example.domain.repository.DataRepository
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : DataRepository {

    override suspend fun getDataList(): NetworkResult<List<DataItem>> =
        safeApiCall { apiService.getDataList().toDomain() }
}
```

---

## 8. Hilt Repository Binding Module (`:data` module)

Bind the repository implementation in Hilt:

```kotlin
package com.example.data.di

import com.example.data.repository.DataRepositoryImpl
import com.example.domain.repository.DataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDataRepository(
        dataRepositoryImpl: DataRepositoryImpl
    ): DataRepository
}
```
