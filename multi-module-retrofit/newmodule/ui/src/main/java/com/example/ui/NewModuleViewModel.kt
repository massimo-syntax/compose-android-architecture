package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Item
import com.example.domain.repository.ItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class NewModuleViewModel @Inject constructor(
    val repo: ItemsRepository
) : ViewModel(){

    private val _uiState = MutableStateFlow<List<Item>>(emptyList())
    val uiState = _uiState.asStateFlow()

    fun getData(){
        viewModelScope.launch {
            val items = repo.getItems()
            _uiState.value = items
        }
    }

    init{
        getData()
    }

}