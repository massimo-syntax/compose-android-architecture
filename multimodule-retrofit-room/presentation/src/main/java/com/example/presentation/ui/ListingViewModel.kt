package com.example.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Science
import com.example.domain.repository.ScienceRepository
import com.example.domain.usecase.GetScienceListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ListingViewModel @Inject constructor(
    private val getListUseCase: GetScienceListUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<List<Science>>(emptyList())
    val uiState = _uiState.asStateFlow()

    init {
        loadList()
    }

    fun loadList(){
        viewModelScope.launch {
            _uiState.value = getListUseCase()
        }
    }
}