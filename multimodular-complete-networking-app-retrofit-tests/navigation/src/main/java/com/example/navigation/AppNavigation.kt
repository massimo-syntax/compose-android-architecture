package com.example.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.feature.details.api.EmailDetailsKey
import com.example.feature.details.impl.emailDetailsEntry
import com.example.feature.list.api.EmailListKey
import com.example.feature.list.impl.emailListEntry

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(EmailListKey)

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            emailListEntry(
                onNavigateToDetails = { id ->
                    backStack.add(EmailDetailsKey(id))
                }
            )
            emailDetailsEntry(
                onNavigateBack = {
                    backStack.removeLastOrNull()
                }
            )
        },
    )
}
