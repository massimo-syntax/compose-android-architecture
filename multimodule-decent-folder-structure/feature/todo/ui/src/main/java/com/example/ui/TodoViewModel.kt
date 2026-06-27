package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Todo
import com.example.domain.repository.TodoRepository
import com.example.domain.usecase.GeTodoListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val todoUseCase : GeTodoListUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<List<Todo>>(emptyList())
    val uiState = _uiState.asStateFlow()


    fun loadTodos(){
        viewModelScope.launch {
            _uiState.value = todoUseCase()
        }
    }

    init{
        loadTodos()
    }

}