package com.example.data_remote.apiResult

import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.CancellationException
import okio.IOException
import retrofit2.HttpException
import java.net.SocketTimeoutException

// taken from
// https://www.linkedin.com/pulse/error-handling-trycatch-safe-api-requests-retrofit-daniel-cardoso-kecgf/

sealed interface ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>
    data class Failure(
        val kind: Kind,
        val code: Int? = null,
        val message: String? = null,
        val cause: Throwable? = null
    ) : ApiResult<Nothing> {
        enum class Kind { Http, Network, Timeout, Parsing, Canceled, Unknown }
    }
}

suspend inline fun <T> trySafeApiCall(block: suspend () -> T): ApiResult<T> =
    try {
        val data = block()
        ApiResult.Success(data)
    } catch (e: CancellationException) {
        ApiResult.Failure(
            ApiResult.Failure.Kind.Canceled,
            message = "Canceled",
            cause = e
        )
    } catch (e: HttpException) {
        ApiResult.Failure(
            ApiResult.Failure.Kind.Http,
            code = e.code(),
            message = e.message(),
            cause = e
        )
    } catch (e: SocketTimeoutException) {
        ApiResult.Failure(
            ApiResult.Failure.Kind.Timeout,
            message = "Request timed out",
            cause = e
        )
    } catch (e: IOException) {
        ApiResult.Failure(
            ApiResult.Failure.Kind.Network,
            message = "Check your connection",
            cause = e
        )
    } catch (e: JsonDataException) { // Moshi parsing error
        ApiResult.Failure(
            ApiResult.Failure.Kind.Parsing,
            message = "Invalid server data",
            cause = e
        )
    } catch (e: Throwable) {
        ApiResult.Failure(
            ApiResult.Failure.Kind.Unknown,
            message = "Something went wrong",
            cause = e
        )
    }