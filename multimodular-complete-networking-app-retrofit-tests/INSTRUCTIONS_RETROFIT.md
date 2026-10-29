# Modern Retrofit Usage Instructions

The Implementation uses  **Hilt** and **Kotlinx Serialization**.

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

## Create some function to grant a clean result from the api request

Create files in a package to contain result related classes, in :core or :data module

### Types to explain network errors on exception (`Failure.kt`):
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

### Result mapper to translate DTO to Domain model (`ResultMapper.kt`):
```kotlin
package com.example.core.networkresponse.mapper

fun interface ResultMapper<T, R> {
    fun map(input: T): R
}
```

### Classes to define Result or Error (`ResultEither.kt`):
```kotlin
package com.example.core.networkresponse.response

// EITHER
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

---

## 5. Safe API Call Implementation (`:data` module)

In the `:data` module under `data.remote.safecall`, create helper functions to execute safe network calls with type-safe results.

### Translate Java / Retrofit exceptions to type-safe errors (`ErrorHandler.kt`):
```kotlin
package com.example.data.remote.safecall

import com.example.core.networkresponse.error.Failure
import com.example.core.networkresponse.response.ResultEither
import retrofit2.HttpException
import java.io.IOException

// translate java exception to type safe error
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

### Function to perform safe network calls returning typed results (`SafeApiCall.kt`):
```kotlin
package com.example.data.remote.safecall

import com.example.core.networkresponse.error.Failure
import com.example.core.networkresponse.mapper.ResultMapper
import com.example.core.networkresponse.response.ResultEither
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// function to receive a segregated type safe result from network request
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
    }.getOrElse {
        // run catching returns an exception
        // parse throwable in type safe error
        it.toEitherResult()
    }
}
```

---

## 6. Domain Models (`:domain` module)

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

## 7. Data Mappers (`:data` module)

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

## 8. Repository Layer & Dependency Injection

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
> If the repository interface returns `Failure Error Success_Type` or `Result_*` classes stored in another module, that module must be added as a dependency in `:domain/build.gradle.kts`. Ideally, the domain module should remain as independent of external dependencies as possible.

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