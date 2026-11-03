package com.example.feature.details.impl

import com.example.core.networkresult.NetworkError
import com.example.domain.model.emaildetails.EmailDetails

sealed interface EmailDetailsState {
    data object Loading : EmailDetailsState
    data class Success(val data: EmailDetails) : EmailDetailsState
    data class Error(val error: NetworkError) : EmailDetailsState
}

sealed interface EmailDetailsEvent {
    data object LoadEmail : EmailDetailsEvent
    data object BackClicked : EmailDetailsEvent
}

sealed interface EmailDetailsEffect {
    data object NavigateBack : EmailDetailsEffect
}
