package com.example.presentation.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.EventualNetworkFeedback
import com.example.domain.model.Science
import com.example.domain.repository.ScienceRepository
import com.example.domain.usecase.GetScienceListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class UiState(
    val loading: Boolean = true,
    val data: List<Science> = emptyList()
)

@HiltViewModel
class ListingViewModel @Inject constructor(
    private val getListUseCase: GetScienceListUseCase
) : ViewModel() {
    val uiState = getListUseCase
        .observeDb()
        .map{
            UiState(false,it)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState()
        )

    val networkError = mutableStateOf(EventualNetworkFeedback(success = true))

    init {
        requestAndSaveData()
    }

    fun requestAndSaveData() {
        viewModelScope.launch {
            networkError.value = getListUseCase()
        }
    }


}