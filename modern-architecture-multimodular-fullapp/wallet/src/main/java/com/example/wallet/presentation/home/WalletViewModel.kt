package com.example.wallet.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.utils.Resource
import com.example.wallet.domain.usecase.GetCoinsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val getCoinsUseCase: GetCoinsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(WalletHomeUiState())
    val uiState: StateFlow<WalletHomeUiState> = _uiState.asStateFlow()

    init{
        viewModelScope.launch(Dispatchers.IO) {
            getCoins()
        }
    }


    private suspend fun getCoins() {
        getCoinsUseCase().onEach { result ->
            when (result) {
                is Resource.Loading -> { _uiState.value = WalletHomeUiState(isLoading = true) }
                is Resource.Success -> {
                    _uiState.value = WalletHomeUiState(
                        isLoading = false,
                        coins = result.data ?: emptyList(),
                        error = null
                    )
                }
                is Resource.Error -> {
                    _uiState.value = WalletHomeUiState(
                        isLoading = false,
                        error = result.message
                    )
                }
            }
        }.launchIn(viewModelScope)

    }

}