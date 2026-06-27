package com.example.domain.usecase

import com.example.domain.model.Todo
import com.example.domain.repository.TodoRepository
import javax.inject.Inject

class GeTodoListUseCase @Inject constructor (
    private val repository: TodoRepository
) {
    suspend operator fun invoke(): List<Todo> = repository.getTodos()
}