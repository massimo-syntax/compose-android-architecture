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
