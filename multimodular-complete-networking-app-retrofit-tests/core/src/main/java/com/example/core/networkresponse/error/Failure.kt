package com.example.core.networkresponse.error

import java.io.IOException

// To parse networking response errors
sealed class Failure  {
    data class NetworkError(val exception : IOException) : Failure()
    data class ServerError(val code : Int, val message : String) : Failure()
    data class UnknownError(val throwable : Throwable) : Failure()
}

fun Failure.getErrorMessage(): String {
    return when (this) {
        is Failure.NetworkError -> "Network error occurred: ${exception.message}"
        is Failure.ServerError -> message
        is Failure.UnknownError -> "An unknown error occurred: ${throwable.message}"
    }
}