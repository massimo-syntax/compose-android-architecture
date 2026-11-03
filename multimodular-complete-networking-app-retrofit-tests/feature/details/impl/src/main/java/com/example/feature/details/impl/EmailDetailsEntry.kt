package com.example.feature.details.impl

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.example.feature.details.api.EmailDetailsKey

fun EntryProviderScope<NavKey>.emailDetailsEntry(
    onNavigateBack: () -> Unit
) {
    entry<EmailDetailsKey> { key ->
        val viewModel = hiltViewModel<EmailDetailsViewModel, EmailDetailsViewModel.Factory>(
            creationCallback = { factory -> factory.create(key.id) }
        )
        EmailDetailsScreen(
            viewModel = viewModel,
            onNavigateBack = onNavigateBack
        )
    }
}
