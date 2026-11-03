package com.example.feature.list.impl

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.example.feature.list.api.EmailListKey

fun EntryProviderScope<NavKey>.emailListEntry(
    onNavigateToDetails: (String) -> Unit
) {
    entry<EmailListKey> {
        HomeScreen(onNavigateToDetails = onNavigateToDetails)
    }
}
