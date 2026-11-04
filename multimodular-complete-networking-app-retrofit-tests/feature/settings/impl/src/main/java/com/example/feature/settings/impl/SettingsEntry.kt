package com.example.feature.settings.impl

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.example.feature.settings.api.SettingsKey

fun EntryProviderScope<NavKey>.settingsEntry() {
    entry<SettingsKey> {
        SettingsScreen()
    }
}
