package com.example.feature.profile.impl

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.example.feature.profile.api.ProfileKey

fun EntryProviderScope<NavKey>.profileEntry() {
    entry<ProfileKey> {
        ProfileScreen()
    }
}
