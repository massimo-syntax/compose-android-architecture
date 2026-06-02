package com.example.hiltretrofit.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hiltretrofit.data.model.SimpleModel
import com.example.hiltretrofit.data.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetworkingScreenViewModel @Inject constructor(
    private val repo: NetworkRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<List<SimpleModel>>(emptyList())
    val uiState = _uiState.asStateFlow()

    fun getData(){
        viewModelScope.launch {
            val data = repo.getData()
            _uiState.update { data }
        }
    }

    init {
        getData()
    }


}