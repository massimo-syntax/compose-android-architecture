package com.example.data.api

import com.example.data.dto.TodoDto
import com.example.domain.model.Todo
import retrofit2.http.GET


interface TodoApi {
    @GET("todos")
    suspend fun getTodos():List<TodoDto>
}