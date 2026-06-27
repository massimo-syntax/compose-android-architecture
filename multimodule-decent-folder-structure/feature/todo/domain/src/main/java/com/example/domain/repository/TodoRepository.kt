package com.example.domain.repository

import com.example.domain.model.Todo

interface TodoRepository{
    suspend fun getTodos():List<Todo>
}