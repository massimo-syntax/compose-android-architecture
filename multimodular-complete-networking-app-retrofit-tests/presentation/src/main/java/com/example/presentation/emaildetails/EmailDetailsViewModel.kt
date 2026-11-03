package com.example.presentation.emaildetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.networkresult.NetworkError
import com.example.core.networkresult.NetworkResult
import com.example.domain.repository.EmailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
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
class EmailDetailsViewModel @Inject constructor(
    private val repo: EmailRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val emailId: String = savedStateHandle.get<String>("id")
        ?: savedStateHandle.get<String>("emailId")
        ?: ""

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
                            val email = result.data.first()//find { it.id == emailId }
                            if (email != null) {
                                _uiState.update { EmailDetailsState.Success(email) }
                            } else {
                                val fallbackEmail = if (emailId.isEmpty()) result.data.firstOrNull() else null
                                if (fallbackEmail != null) {
                                    _uiState.update { EmailDetailsState.Success(fallbackEmail) }
                                } else {
                                    _uiState.update { EmailDetailsState.Error(NetworkError.NotFound) }
                                }
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
