package com.example.presentation.emaillist

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.networkresponse.response.fold
import com.example.domain.repository.EmailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    val repo: EmailRepository
) : ViewModel(){

    private val _uiState = MutableStateFlow<EmailListState>(EmailListState.Loading)
    val uiState = _uiState.asStateFlow()
        .onStart { event(EmailListEvent.LoadEmailList) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            EmailListState.Loading
        )

    private val _effect: MutableSharedFlow<EmailListEffect> = MutableSharedFlow(replay = 0)
    val effect = _effect.asSharedFlow()

    fun event(event: EmailListEvent){
        when(event){
            is EmailListEvent.EmailClicked -> {
                viewModelScope.launch {
                    _effect.emit(EmailListEffect.NavigateToEmailDetails(event.model))
                }
            }
            is EmailListEvent.LoadEmailList -> {
                viewModelScope.launch {
                    val result = repo.getEmailList()
                    result.fold(
                        onError = { error ->
                            _uiState.update { EmailListState.Error(error) }
                        },
                        onSuccess = { list ->
                            _uiState.update { EmailListState.Success(list) }
                        }
                    )
                }
            }
        }
    }

}