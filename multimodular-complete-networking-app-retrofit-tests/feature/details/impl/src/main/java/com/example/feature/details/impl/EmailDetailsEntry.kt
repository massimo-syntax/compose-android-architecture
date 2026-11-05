package com.example.feature.details.impl

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.example.feature.details.api.EmailDetailsKey
import com.example.navigation.api.Navigator

fun EntryProviderScope<NavKey>.emailDetailsEntry(
    appNavigator: Navigator
) {
    entry<EmailDetailsKey> { key ->
        val viewModel = hiltViewModel<EmailDetailsViewModel, EmailDetailsViewModel.Factory>(
            creationCallback = { factory -> factory.create(key.id) }
        )
        EmailDetailsScreen(
            viewModel = viewModel,
            onNavigateBack = appNavigator::goBack
        )
    }
}
