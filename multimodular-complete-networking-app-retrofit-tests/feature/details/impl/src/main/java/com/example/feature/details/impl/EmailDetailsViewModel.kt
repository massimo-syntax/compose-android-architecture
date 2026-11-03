package com.example.feature.details.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.networkresult.NetworkError
import com.example.core.networkresult.NetworkResult
import com.example.domain.repository.EmailRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = EmailDetailsViewModel.Factory::class)
class EmailDetailsViewModel @AssistedInject constructor(
    private val repo: EmailRepository,
    @Assisted val emailId: String
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(emailId: String): EmailDetailsViewModel
    }

    private val _uiState = MutableStateFlow<EmailDetailsState>(EmailDetailsState.Loading)
    val uiState = _uiState.asStateFlow()
        .onStart { event(EmailDetailsEvent.LoadEmail) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = EmailDetailsState.Loading
        )

    private val _effect: MutableSharedFlow<EmailDetailsEffect> = MutableSharedFlow(replay = 0)
    val effect = _effect.asSharedFlow()

    fun event(event: EmailDetailsEvent) {
        when (event) {
            is EmailDetailsEvent.BackClicked -> {
                viewModelScope.launch {
                    _effect.emit(EmailDetailsEffect.NavigateBack)
                }
            }

            is EmailDetailsEvent.LoadEmail -> {
                viewModelScope.launch {
                    when (val result = repo.getEmailDetail()) {
                        is NetworkResult.Success -> {
                            val email = result.data.find { it.id == emailId }
                                ?: result.data.firstOrNull()
                            if (email != null) {
                                _uiState.update { EmailDetailsState.Success(email) }
                            } else {
                                _uiState.update { EmailDetailsState.Error(NetworkError.NotFound) }
                            }
                        }

                        is NetworkResult.Failure -> {
                            _uiState.update { EmailDetailsState.Error(result.error) }
                        }
                    }
                }
            }
        }
    }
}
