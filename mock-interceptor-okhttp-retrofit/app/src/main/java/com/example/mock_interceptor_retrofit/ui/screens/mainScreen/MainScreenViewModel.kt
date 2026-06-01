package com.example.mock_interceptor_retrofit.ui.screens.mainScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mock_interceptor_retrofit.remote.retrofilt.MockApiService
import com.example.mock_interceptor_retrofit.remote.retrofilt.ResponseModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val mockRetrofit : MockApiService
) : ViewModel() {
    private val _uiState = MutableStateFlow<List<ResponseModel>>(emptyList())
    val uiState = _uiState.asStateFlow()


    fun getData(){
        Log.e("UserViewModel", "GET DATA CLICKED")

        viewModelScope.launch {
            try {
                val result = mockRetrofit.getData()
                _uiState.update { result }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Error: ${e.localizedMessage}")
            }
        }
    }

    fun getOther(){
        viewModelScope.launch {
            try {
                val result = mockRetrofit.getOther()
                _uiState.update { result }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Error: ${e.localizedMessage}")
            }
        }
    }



}