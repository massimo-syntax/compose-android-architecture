package com.example.data.remote.safecall

import com.example.core.networkresponse.error.Failure
import com.example.core.networkresponse.response.ResultEither
import retrofit2.HttpException
import java.io.IOException

// translate to type safe error
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