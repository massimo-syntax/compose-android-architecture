package com.example.feature.list.impl

import com.example.core.networkresponse.error.Failure
import com.example.domain.model.emaillist.EmailListItem

sealed interface EmailListState {
    data object Loading : EmailListState
    data class Success(val data: List<EmailListItem>) : EmailListState
    data class Error(val error: Failure) : EmailListState
}

sealed interface EmailListEvent {
    data object LoadEmailList : EmailListEvent
    data class EmailClicked(val model: EmailListItem) : EmailListEvent
}

sealed interface EmailListEffect {
    data class NavigateToEmailDetails(val model: EmailListItem) : EmailListEffect
}
