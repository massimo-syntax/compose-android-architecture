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
    mapper: (T) -> R,
): ResultEither<Failure, R> {
    return withContext(ioDispatcher) {
        runCatching {
            val response = apiCall()
            ResultEither.Success(mapper(response))
        }
    }.getOrElse {
        // run catching returns an exception
        // parse throwable in type safe error
        it.toEitherResult()
    }
}