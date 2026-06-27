package com.example.data.mapper

import com.example.data.dto.TodoDto
import com.example.domain.model.Todo

fun TodoDto.toDomain(): Todo = Todo(id = id, title = title, completed = true)
fun List<TodoDto>.toDomain() = map{ it.toDomain() }
