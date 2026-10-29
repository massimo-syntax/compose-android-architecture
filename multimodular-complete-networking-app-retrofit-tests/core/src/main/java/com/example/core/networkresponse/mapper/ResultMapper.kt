package com.example.core.networkresponse.mapper

fun interface ResultMapper<T, R> {
    fun map(input: T): R
}