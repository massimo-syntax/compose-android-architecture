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

## 4. Approaches to Return a Type-Safe API Result

To handle network requests cleanly and return typed results instead of raw exceptions, choose one of the two approaches below based on project needs:

- **Approach A: Functional / Mapper-Based (`ResultEither` + `ResultMapper`)** — Suitable for larger apps requiring structured error types and decoupled DTO-to-Domain mapping.
- **Approach B: Lightweight / Direct (`ApiResult` + `NetworkError`)** — A simpler, lightweight alternative that encapsulates errors directly without needing dedicated mapper interfaces.

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

### Approach B: Lightweight / Direct (`ApiResult`)

A simpler, lightweight approach that handles success and network errors directly. The code can be kept together or placed into separate files as needed.

#### 1. Network Error & Result Definitions (`ApiResult.kt`):

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

sealed interface ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>
    data class Failure(val error: NetworkError) : ApiResult<Nothing>
}
```

#### 2. Safe API Call Execution & Error Mapping (`SafeApiCall.kt`):

```kotlin
suspend fun <T> safeApiCall(block: suspend () -> T): ApiResult<T> =
    try {
        ApiResult.Success(block())
    } catch (e: CancellationException) {
        throw e // Never swallow Coroutine CancellationException
    } catch (e: Throwable) {
        ApiResult.Failure(e.toNetworkError())
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
    is kotlinx.serialization.SerializationException, // kotlinx.serialization
    is com.squareup.moshi.JsonDataException,          // Moshi
    is com.google.gson.JsonParseException -> NetworkError.Serialization // Gson
    else -> NetworkError.Unknown(this)
}
```

#### 3. DTO to Domain Mapping via Extension Functions

To map DTOs into Domain models, extension functions can be declared directly inside the DTO file (typically located in the `:data` module).

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

This transforms raw DTO models into Domain models (usually stored in the `:domain` module) which are then accessed by ViewModels and Composables.

In the repository implementation, execute the safe API call and map directly:

```kotlin
override suspend fun getMyData(): ApiResult<MyData> = safeApiCall {
    apiService.getData().toDomain()
}
```

---

## 5. Domain Models (`:domain` module)

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

---

## 6. Data Mappers (`:data` module)

Create the mappers in the `:data` module under the `mapper` package.

### Data List Mapper (`DataListMapper.kt`)
```kotlin
package com.example.data.mapper

import com.example.core.networkresponse.mapper.ResultMapper
import com.example.core.networkresponse.orDefault
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
        }.sortedBy {
            it.timestamp
        }.reversed()

    private fun DataItemDto.toDomain() =
        DataItem(
            id = id!!,
            title = title.orEmpty(),
            from = from ?: "Default Value",
            image = image.orEmpty(),
            timestamp = timestamp ?: 0L,
            favorite = favorite.orDefault(false)
        )
}
```

### Data Details Mapper (`DataDetailsMapper.kt`)
```kotlin
package com.example.data.mapper

import com.example.core.networkresponse.mapper.ResultMapper
import com.example.data.model.dto.DataDetailsDto
import com.example.data.model.dto.PayloadDto
import com.example.domain.model.DataDetails
import com.example.domain.model.Payload
import javax.inject.Inject

class DataDetailsMapper @Inject constructor() :
    ResultMapper<List<DataDetailsDto>, List<DataDetails>>
{
    override fun map(input: List<DataDetailsDto>): List<DataDetails> =
        input.filter {
            it.id != null
        }.map {
            it.toDomain()
        }

    private fun DataDetailsDto.toDomain() =
        DataDetails(
            id = id ?: "404",
            body = body.orEmpty(),
            image = image.orEmpty(),
            payload = payload?.toDomain(),
            title = title.orEmpty()
        )

    private fun PayloadDto.toDomain() =
        Payload(
            email = email.orEmpty(),
            from = from.orEmpty(),
            timestamp = timestamp ?: 0L
        )
}
```

### Alternative Mapping Approaches

Alternatively, as a mapper you can also use Kotlin's built-in mapping functions:

```kotlin
// Map directly on the result list
someListResult.map { it.toDomain() }
```

Or pass a transformation lambda to the function executing the network call or returning the value:

```kotlin
// Pass transformation lambda to the safe network request
someSafeNetworkRequest { dto -> dto.toDomain() }
```

### Mapper Location Strategy

Mappers can be declared in separate mapper files (like `DataDetailsMapper.kt`) or placed directly in the same file as the model as extension functions:

```kotlin
fun DataDetailsDto.toDomain() = DataDetails(
    id = id ?: "404",
    title = title.orEmpty(),
    body = body.orEmpty(),
    image = image.orEmpty(),
    payload = payload?.toDomain()
)

fun List<DataDetailsDto>.toDomain(): List<DataDetails> = map { it.toDomain() }
```

---

## 7. Repository Layer & Dependency Injection

### Repository Interface (`:domain` module)

Create the `DataRepository` interface in the `:domain` module under `domain.repository`:

```kotlin
package com.example.domain.repository

import com.example.core.networkresponse.error.Failure
import com.example.core.networkresponse.response.ResultEither
import com.example.domain.model.DataDetails
import com.example.domain.model.DataItem

interface DataRepository {
    suspend fun getDataList(): ResultEither<Failure, List<DataItem>>
    suspend fun getDataDetails(): ResultEither<Failure, List<DataDetails>>
}
```

> [!WARNING]
> If the repository interface returns `Failure`, `Error`, `Success_Type`, or `Result_*` classes stored in another module, that module must be added as a dependency in `:domain/build.gradle.kts`. Ideally, the domain module should remain as independent of external dependencies as possible.

### Repository Implementation (`:data` module)

Create `DataRepositoryImpl` in the `:data` module under `data.repository`:

```kotlin
package com.example.data.repository

import com.example.core.networkresponse.error.Failure
import com.example.core.networkresponse.response.ResultEither
import com.example.data.mapper.DataDetailsMapper
import com.example.data.mapper.DataListMapper
import com.example.data.remote.api.ApiService
import com.example.data.remote.safecall.safeApiCall
import com.example.domain.model.DataDetails
import com.example.domain.model.DataItem
import com.example.domain.repository.DataRepository
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val dataListMapper: DataListMapper,
    private val dataDetailsMapper: DataDetailsMapper
) : DataRepository {

    override suspend fun getDataList(): ResultEither<Failure, List<DataItem>> {
        return safeApiCall(
            apiCall = { apiService.getDataList() },
            mapper = { dataListMapper.map(it) }
        )
    }

    override suspend fun getDataDetails(): ResultEither<Failure, List<DataDetails>> =
        safeApiCall(
            apiCall = { apiService.getDataDetails() },
            mapper = { dataDetailsMapper.map(it) }
            // Alternatively: mapper = { dtos -> dtos.toDomain() }
        )
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
