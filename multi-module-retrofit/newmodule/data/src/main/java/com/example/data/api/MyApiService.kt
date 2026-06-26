package com.example.data.api

import com.example.domain.model.Item
import retrofit2.http.GET

interface MyApiService {
    @GET("todos")
    suspend fun getData(): List<Item>
}