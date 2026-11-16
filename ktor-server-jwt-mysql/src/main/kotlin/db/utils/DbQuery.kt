package com.example.db.utils

import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction

suspend fun <T : Any> dbQuery(block: suspend () -> T?): T? =
    suspendTransaction { block() }