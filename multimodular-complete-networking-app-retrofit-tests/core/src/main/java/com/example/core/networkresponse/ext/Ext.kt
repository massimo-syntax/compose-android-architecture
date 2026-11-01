package com.example.core.networkresponse

fun Boolean?.orDefault(default : Boolean = false) : Boolean = this ?: default

// returns a default list of domain model type
fun <T,R : Any> List<T>?.mapOrDefault(defaultListValue : List<R> = emptyList(), transform: (T) -> R): List<R> {
    return this?.filterNotNull()?.map(transform) ?: defaultListValue
}