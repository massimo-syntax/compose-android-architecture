package com.example.navigation.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

enum class MainTab(val segment: String) {
    EMAILLIST("emaillist"),
    PROFILE("profile"),
    SETTINGS("settings");

    companion object {
        fun from(segment: String?): MainTab =
            entries.firstOrNull { it.segment.equals(segment, ignoreCase = true) } ?: EMAILLIST
    }
}

@Serializable
data class MainKey(
    val tab: MainTab = MainTab.EMAILLIST,
    val subTab: String? = null
) : NavKey
