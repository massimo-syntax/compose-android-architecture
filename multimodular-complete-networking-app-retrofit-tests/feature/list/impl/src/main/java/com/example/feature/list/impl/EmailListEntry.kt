package com.example.feature.list.impl

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.example.feature.details.api.EmailDetailsKey
import com.example.feature.list.api.EmailListKey
import com.example.navigation.api.Navigator

fun EntryProviderScope<NavKey>.emailListEntry(
    appNavigator: Navigator
) {
    entry<EmailListKey> {
        HomeScreen(
            onNavigateToDetails = { id ->
                appNavigator.navigate(EmailDetailsKey(id))
            }
        )
    }
}
