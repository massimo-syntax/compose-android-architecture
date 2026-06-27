package com.example.data.repository

import com.example.data.api.TodoApi
import com.example.data.mapper.toDomain
import com.example.domain.model.Todo
import com.example.domain.repository.TodoRepository
import jakarta.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val todoApi: TodoApi
) : TodoRepository{
    override suspend fun getTodos(): List<Todo> = todoApi.getTodos().toDomain()
}