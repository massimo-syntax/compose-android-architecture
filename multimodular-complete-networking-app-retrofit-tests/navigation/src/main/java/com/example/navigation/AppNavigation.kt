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
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.feature.details.impl.emailDetailsEntry
import com.example.feature.list.api.EmailListKey
import com.example.feature.list.impl.emailListEntry
import com.example.feature.login.impl.loginEntry
import com.example.feature.profile.api.ProfileKey
import com.example.feature.profile.api.ProfileSubTab
import com.example.feature.profile.impl.profileEntry
import com.example.feature.settings.api.SettingsKey
import com.example.feature.settings.impl.settingsEntry
import com.example.navigation.api.MainKey
import com.example.navigation.api.MainTab
import com.example.navigation.api.Navigator

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    deepLink: String? = "deeplink://emaillist/emaildetails/email-001"
) {
    val deepLinkPath = remember(deepLink) { DeepLinkPath(deepLink).stack }
    val backStack = rememberNavBackStack(*deepLinkPath.toTypedArray())
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
                onLoginClick = {
                    val path = DeepLinkPath(deepLink)
                    appNavigator.setStack(path.stack)
                }
            )
            emailDetailsEntry(appNavigator)
            entry<MainKey> { key ->
                MainScaffold(
                    initialTab = key.tab,
                    initialSubTab = key.subTab,
                    appNavigator = appNavigator
                )
            }
        },
    )
}

private val tabs = listOf(MainTab.EMAILLIST, MainTab.PROFILE, MainTab.SETTINGS)

@Composable
fun MainScaffold(
    appNavigator: Navigator,
    modifier: Modifier = Modifier,
    initialTab: MainTab = MainTab.EMAILLIST,
    initialSubTab: String? = null
) {
    val tabBackStack = rememberNavBackStack(initialTab.toKey(initialSubTab))
    val currentKey = tabBackStack.lastOrNull() ?: initialTab.toKey(initialSubTab)
    val currentTab = currentKey.toTab()

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar {
                tabs.forEach { tab ->
                    NavigationBarItem(
                        selected = currentTab == tab,
                        onClick = {
                            if (currentTab != tab) {
                                tabBackStack.clear()
                                tabBackStack.add(tab.toKey(null))
                            }
                        },
                        icon = {
                            val icon = when (tab) {
                                MainTab.EMAILLIST -> Icons.Default.Email
                                MainTab.PROFILE -> Icons.Default.Person
                                MainTab.SETTINGS -> Icons.Default.Settings
                            }
                            Icon(imageVector = icon, contentDescription = null)
                        },
                        label = {
                            val label = when (tab) {
                                MainTab.EMAILLIST -> "Emails"
                                MainTab.PROFILE -> "Profile"
                                MainTab.SETTINGS -> "Settings"
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
                emailListEntry(appNavigator)
                profileEntry()
                settingsEntry()
            }
        )
    }
}

private fun MainTab.toKey(subTab: String?): NavKey = when (this) {
    MainTab.EMAILLIST -> EmailListKey
    MainTab.PROFILE -> ProfileKey(ProfileSubTab.from(subTab))
    MainTab.SETTINGS -> SettingsKey
}

private fun NavKey.toTab(): MainTab = when (this) {
    is ProfileKey -> MainTab.PROFILE
    is SettingsKey -> MainTab.SETTINGS
    else -> MainTab.EMAILLIST
}
