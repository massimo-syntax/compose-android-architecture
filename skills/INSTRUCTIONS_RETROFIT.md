# Modern Retrofit Usage Instructions

The implementation uses **Hilt** and **Kotlinx Serialization**.

---

## 1. Dependencies Setup

Add the required dependencies and plugins to `gradle/libs.versions.toml`:

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

> **Note:** Plugins must be declared in the root `build.gradle.kts` with `apply false` and applied in the corresponding module's `build.gradle.kts`.

---

## 2. API Service Interface

Create the `ApiService` interface in the `:data` module under `data.remote.api`:

```kotlin
package com.example.data.remote.api

import com.example.data.dto.DataDetailDto
import com.example.data.dto.DataItemDto
import retrofit2.http.GET

interface ApiService {
    @GET("api/v1/items")
    suspend fun getDataList(): List<DataItemDto>

    @GET("api/v1/details")
    suspend fun getDataDetails(): List<DataDetailDto>
}
```

---

## 3. Hilt Network Module

Create `NetworkModule` inside the `di` package, usually of the `:data` module:

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

    // Configure Json parser to ignore unmapped API fields
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
            // Convert Kotlinx Serialization Json to a Retrofit ConverterFactory
            .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
            .client(OkHttpClient.Builder().addInterceptor(loggingInterceptor).build())
            .build()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)
}
```

### Key Nuances:
- **`ignoreUnknownKeys = true`**: Prevents app crashes if the API returns new or unmapped JSON keys.
- **`asConverterFactory("application/json".toMediaType())`**: Bridges Kotlinx Serialization with Retrofit using explicit `"application/json"` media type conversion.

---

## Create Models, DTO and "domain" (`:domain` module)

In the `:domain` layer (or package responsible for domain code), create the models used for the rest of the application that are not directly tied to the raw network response.

In the `model` package, create data classes with fields needed by the application (or with matching fields/subclasses as the DTO models).

### Example:

**`:data` module or package (DTO model used to parse JSON into Kotlin data classes):**
```kotlin
@Serializable
data class DataDto(
    val id: String,
    val title: String,
    val description: String,
    val magicNumber: Int
)
```

**`:domain` module or package (Domain model used across the rest of the app):**
```kotlin
data class Data(
    val id: String,
    val title: String,
    val description: String,
    // No magic number in this case, for example
)
```






## 4. Approaches to Return a Type-Safe API Result

To handle network requests cleanly and return typed results instead of raw exceptions, choose one of the two approaches below based on project needs:

- **Approach A: Functional / Mapper-Based (`ResultEither` + `ResultMapper`)** — Suitable for larger apps requiring structured error types and decoupled DTO-to-Domain mapping classes.
- **Approach B: Lightweight / Direct (`NetworkResult` + `NetworkError`)** — A simpler, lightweight alternative that encapsulates errors directly without needing dedicated mapper interfaces.

---

### Approach A: Functional / Mapper-Based (`ResultEither`)

Create helper files in `:core` or `:data` module.

#### 1. Network Failure Representation (`Failure.kt`):
```kotlin
package com.example.core.networkresponse.error

import java.io.IOException

// To parse networking response errors
sealed class Failure {
    data class NetworkError(val exception: IOException) : Failure()
    data class ServerError(val code: Int, val message: String) : Failure()
    data class UnknownError(val throwable: Throwable) : Failure()
}

fun Failure.getErrorMessage(): String {
    return when (this) {
        is Failure.NetworkError -> "Network error occurred: ${exception.message}"
        is Failure.ServerError -> message
        is Failure.UnknownError -> "An unknown error occurred: ${throwable.message}"
    }
}
```

#### 2. Result Mapper Interface (`ResultMapper.kt`):
```kotlin
package com.example.core.networkresponse.mapper

fun interface ResultMapper<T, R> {
    fun map(input: T): R
}
```

#### 3. Either Result Type (`ResultEither.kt`):
```kotlin
package com.example.core.networkresponse.response

sealed class ResultEither<out E, out S> {
    data class Error<out T>(val value: T) : ResultEither<T, Nothing>()
    data class Success<out T>(val value: T) : ResultEither<Nothing, T>()
}

inline fun <S, E, T> ResultEither<E, S>.fold(
    onError: (E) -> T,
    onSuccess: (S) -> T,
) = when (this) {
    is ResultEither.Error -> onError(value)
    is ResultEither.Success -> onSuccess(value)
}
```

#### 4. Safe API Execution & Error Handler (`ErrorHandler.kt` & `SafeApiCall.kt`):
```kotlin
package com.example.data.remote.safecall

import com.example.core.networkresponse.error.Failure
import com.example.core.networkresponse.response.ResultEither
import retrofit2.HttpException
import java.io.IOException

fun Throwable.toEitherResult(): ResultEither<Failure, Nothing> {
    return when (this) {
        is IOException -> ResultEither.Error(Failure.NetworkError(this))
        is HttpException -> {
            val code = code()
            val message = message()
            ResultEither.Error(Failure.ServerError(code, message))
        }
        else -> ResultEither.Error(Failure.UnknownError(this))
    }
}
```


### Class Mappers implementing `ResultMapper`
Create the mappers in the `:data` module under the `mapper` package.

```kotlin
package com.example.data.mapper

import com.example.core.networkresponse.mapper.ResultMapper
import com.example.data.model.dto.DataItemDto
import com.example.domain.model.DataItem
import javax.inject.Inject

class DataListMapper @Inject constructor() :
    ResultMapper<List<DataItemDto>, List<DataItem>>
{
    override fun map(input: List<DataItemDto>): List<DataItem> =
        input.filter {
            it.id != null
        }.map {
            it.toDomain()
        }

    private fun DataItemDto.toDomain() =
        DataItem(
            id = id ?: "",
            title = title.orEmpty(),
            description = desc.orEmpty()
        )
}
```

#### Safe API call

```kotlin
package com.example.data.remote.safecall

import com.example.core.networkresponse.error.Failure
import com.example.core.networkresponse.mapper.ResultMapper
import com.example.core.networkresponse.response.ResultEither
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T, R> safeApiCall(
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    apiCall: suspend () -> T,
    mapper: ResultMapper<T, R>
): ResultEither<Failure, R> {
    return withContext(ioDispatcher) {
        runCatching {
            val response = apiCall()
            ResultEither.Success(mapper.map(response))
        }
    }.getOrElse { throwable ->
        throwable.toEitherResult()
    }
}
```

---

### Approach B: Lightweight / Direct (`NetworkResult`)

A simpler, lightweight approach that handles success and network errors directly with a single generic parameter `NetworkResult<T>`.

> [!TIP]
> Place `NetworkResult` and `NetworkError` in the `:core` module (`com.example.core.networkresult`) so `:domain`, `:data`, and UI modules can access them directly without circular dependencies.

#### 1. Network Error & Result Definitions (`:core` module -> `NetworkResult.kt`):

```kotlin
import java.io.IOException
import java.net.SocketTimeoutException
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException

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

#### 2. Safe API Execution & Throwable Mapping (`:data` module -> `SafeApiCall_.kt`):

```kotlin
package com.example.data.remote.safecall

import com.example.core.networkresult.NetworkError
import com.example.core.networkresult.NetworkResult
import java.io.IOException
import java.net.SocketTimeoutException
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import retrofit2.HttpException

suspend fun <T> safeApiCall_(block: suspend () -> T): NetworkResult<T> =
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
    is SocketTimeoutException -> NetworkError.Timeout // Must come before IOException
    is IOException -> NetworkError.NoInternet
    is SerializationException, // Kotlinx Serialization
    // is JsonDataException,    // Moshi (uncomment if using Moshi)
    // is JsonParseException    // Gson (uncomment if using Gson)
    -> NetworkError.Serialization
    else -> NetworkError.Unknown(this)
}
```

#### 3. DTO to Domain Mapping via Extension Functions (`:data` module -> `Mappers.kt`):

```kotlin
@Serializable
data class MyDataDto(
    val a: String,
    val b: String,
    val z: List<MoreDataDto>
)

@Serializable
data class MoreDataDto(
    val x: Int,
    val y: Double
)

fun MoreDataDto.toDomain() = MyMoreData(
    x = x,
    y = y
)

fun List<MoreDataDto>.toDomain() = map { it.toDomain() }

fun MyDataDto.toDomain() = MyData(
    a = a,
    b = b,
    z = z.toDomain()
)
```

In the repository implementation, execute the safe API call and map directly:

```kotlin
override suspend fun getMyData(): NetworkResult<MyData> = safeApiCall_ {
    apiService.getData().toDomain()
}
```


---

## 7. Repository Layer & Dependency Injection

### Repository Interface (`:domain` module)

```kotlin
package com.example.domain.repository

import com.example.core.networkresponse.error.Failure
import com.example.core.networkresponse.response.ResultEither
import com.example.core.networkresult.NetworkResult
import com.example.domain.model.DataDetails
import com.example.domain.model.DataItem

interface DataRepository {
    // Approach A
    suspend fun getDataListWithResultEither(): ResultEither<Failure, List<DataItem>>

    // Approach B (Lightweight)
    suspend fun getDataList(): NetworkResult<List<DataItem>>
}
```

### Repository Implementation (`:data` module)

```kotlin
package com.example.data.repository

import com.example.core.networkresponse.error.Failure
import com.example.core.networkresponse.response.ResultEither
import com.example.core.networkresult.NetworkResult
import com.example.data.mapper.DataListMapper
import com.example.data.mapper.toDomain
import com.example.data.remote.api.ApiService
import com.example.data.remote.safecall.safeApiCall
import com.example.data.remote.safecall.safeApiCall_
import com.example.domain.model.DataItem
import com.example.domain.repository.DataRepository
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val dataListMapper: DataListMapper // Needed for Approach A
) : DataRepository {

    // Approach A: Class mapper with safeApiCall returning ResultEither
    override suspend fun getDataListWithResultEither(): ResultEither<Failure, List<DataItem>> {
        return safeApiCall(
            apiCall = { apiService.getDataList() },
            mapper = { dataListMapper.map(it) }
        )
    }

    // Approach B: Extension mapper with safeApiCall_ returning NetworkResult
    override suspend fun getDataList(): NetworkResult<List<DataItem>> =
        safeApiCall_ { apiService.getDataList().toDomain() }
}
```

### Hilt Repository Module (`:data` module)

Bind the repository implementation in the `:data` module under `data.di`:

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
