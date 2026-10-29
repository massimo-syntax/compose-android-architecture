package com.example.core.networkresponse.response


// EITHER
sealed class ResultEither<out E, out S> {
    data class Error<out T>(val value:T) : ResultEither<T, Nothing>()
    data class Success<out T>(val value:T) : ResultEither<Nothing, T>()
}

inline fun <S, E, T> ResultEither<E,S>.fold(
    onError: (E) -> T,
    onSuccess: (S) -> T,
)  = when(this) {
    is ResultEither.Error -> onError(value)
    is ResultEither.Success -> onSuccess(value)
}