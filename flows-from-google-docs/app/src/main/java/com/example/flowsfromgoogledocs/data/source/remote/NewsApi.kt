package com.example.flowsfromgoogledocs.data.source.remote

import com.example.flowsfromgoogledocs.data.model.New

// Interface that provides a way to make network requests with suspend functions
interface NewsApi {
    suspend fun fetchLatestNews(): List<New>
}