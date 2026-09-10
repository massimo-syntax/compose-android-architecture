package com.example.wallet.presentation.screens.coindetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.util.Logger
import com.example.utils.Resource
import com.example.utils.logging.AppLogger
import com.example.wallet.domain.usecase.GetCoinDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class CoinDetailsViewModel @Inject constructor(
    private val getCoinDetailsUseCase: GetCoinDetailsUseCase
): ViewModel(){

    private val _uiState = MutableStateFlow(CoinDetailsUiState())
    val uiState: StateFlow<CoinDetailsUiState> = _uiState.asStateFlow()

    suspend fun getCoinDetails(coinId: String){
        getCoinDetailsUseCase(coinId).onEach { result ->
            when (result) {
                is Resource.Loading -> { _uiState.value = CoinDetailsUiState(isLoading = true) }
                is Resource.Success -> {
                    _uiState.value = CoinDetailsUiState(coin = result.data)
                }
                is Resource.Error -> {
                    _uiState.value = CoinDetailsUiState(error = result.message)
                }
            }
        }.launchIn(viewModelScope)

    }

}

