package com.example.wallet.presentation.screens.coindetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.util.Logger
import com.example.utils.Resource
import com.example.utils.logging.AppLogger
import com.example.wallet.domain.model.CoinTicker
import com.example.wallet.domain.usecase.GetCoinDetailsUseCase
import com.example.wallet.domain.usecase.GetCoinTickerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CoinDetailsViewModel @Inject constructor(
    private val getCoinDetailsUseCase: GetCoinDetailsUseCase,
    private val getCoinTickerUseCase: GetCoinTickerUseCase
): ViewModel(){

    private val _uiState = MutableStateFlow(CoinDetailsUiState())
    val uiState: StateFlow<CoinDetailsUiState> = _uiState.asStateFlow()

    suspend fun getCoinDetails(coinId: String){
        getCoinDetailsUseCase(coinId).onEach { result ->
            when (result) {
                is Resource.Loading -> { _uiState.value = CoinDetailsUiState(isLoading = true) }
                is Resource.Success -> {
                    // update state
                    _uiState.value = CoinDetailsUiState(coin = result.data)
                    // request other info such as price,
                    // of course sequentially
                    // i know is not the best practice here, im trying different architectures strategies
                    getCoinTicker(coinId)
                }
                is Resource.Error -> {
                    _uiState.value = CoinDetailsUiState(error = result.message)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getCoinTicker(id: String){
        viewModelScope.launch {
            val result = getCoinTickerUseCase(id)
            when (result) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    _uiState.update { it.copy(ticker = result.data) }
                }
                is Resource.Error -> {
                    // without price state is reset to error, the whole, also whats about the coin basic info
                    // just testing to try different data flow structures
                    _uiState.value = CoinDetailsUiState(error = result.message)
                }
            }
        }
    }

}

