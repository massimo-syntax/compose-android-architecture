package com.example.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"

private fun dateFormatter(): SimpleDateFormat {
    return SimpleDateFormat(DATE_FORMAT, Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
        isLenient = false
    }
}

fun dateToMillis(value: String): Long {
    return dateFormatter()
        .parse(value)
        ?.time
        ?: throw IllegalArgumentException("Invalid date: $value")
}

fun millisToDate(millis: Long): String {
    return dateFormatter().format(Date(millis))
}

