package com.example.feature.login.impl

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.example.feature.login.api.LoginKey

fun EntryProviderScope<NavKey>.loginEntry(
    onLoginClick: () -> Unit
) {
    entry<LoginKey> {
        LoginScreen(onLoginClick = onLoginClick)
    }
}
