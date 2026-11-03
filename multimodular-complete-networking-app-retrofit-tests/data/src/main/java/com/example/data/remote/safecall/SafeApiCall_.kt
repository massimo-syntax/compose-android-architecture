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
        throw e
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
    is SerializationException -> NetworkError.Serialization
    else -> NetworkError.Unknown(this)
}
