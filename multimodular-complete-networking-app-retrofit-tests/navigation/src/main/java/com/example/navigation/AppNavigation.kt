package com.example.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.feature.details.api.EmailDetailsKey
import com.example.feature.details.impl.emailDetailsEntry
import com.example.feature.list.api.EmailListKey
import com.example.feature.list.impl.emailListEntry
import com.example.feature.login.api.LoginKey
import com.example.feature.login.impl.loginEntry
import com.example.feature.profile.api.ProfileKey
import com.example.feature.profile.impl.profileEntry
import com.example.feature.settings.api.SettingsKey
import com.example.feature.settings.impl.settingsEntry
import kotlinx.serialization.Serializable

@Serializable
data object MainKey : NavKey

class Navigator(private val backStack: NavBackStack<NavKey>) {
    fun navigate(key: NavKey) {
        backStack.add(key)
    }

    fun goBack() {
        backStack.removeLastOrNull()
    }

    fun replaceAll(key: NavKey) {
        backStack.clear()
        backStack.add(key)
    }
}

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(LoginKey)
    val appNavigator = remember(backStack) { Navigator(backStack) }

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = appNavigator::goBack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            loginEntry(
                onLoginClick = { appNavigator.replaceAll(MainKey) }
            )
            emailDetailsEntry(
                onNavigateBack = appNavigator::goBack
            )
            entry<MainKey> {
                MainScaffold(appNavigator = appNavigator)
            }
        },
    )
}

private val tabs = listOf(EmailListKey, ProfileKey, SettingsKey)

@Composable
fun MainScaffold(
    appNavigator: Navigator,
    modifier: Modifier = Modifier
) {
    val tabBackStack = rememberNavBackStack(EmailListKey)
    val currentTab = tabBackStack.lastOrNull() ?: EmailListKey

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar {
                tabs.forEach { tab ->
                    NavigationBarItem(
                        selected = currentTab == tab,
                        onClick = {
                            if (currentTab != tab) {
                                tabBackStack[0] = tab
                            }
                        },
                        icon = {
                            val icon = when (tab) {
                                is EmailListKey -> Icons.Default.Email
                                is ProfileKey -> Icons.Default.Person
                                is SettingsKey -> Icons.Default.Settings
                                else -> Icons.Default.Email
                            }
                            Icon(imageVector = icon, contentDescription = null)
                        },
                        label = {
                            val label = when (tab) {
                                is EmailListKey -> "Emails"
                                is ProfileKey -> "Profile"
                                is SettingsKey -> "Settings"
                                else -> ""
                            }
                            Text(label)
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavDisplay(
            modifier = Modifier.padding(paddingValues),
            backStack = tabBackStack,
            onBack = { /* single entry in inner display, never pops */ },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            entryProvider = entryProvider {
                emailListEntry(
                    onNavigateToDetails = { id ->
                        appNavigator.navigate(EmailDetailsKey(id))
                    }
                )
                profileEntry()
                settingsEntry()
            }
        )
    }
}
