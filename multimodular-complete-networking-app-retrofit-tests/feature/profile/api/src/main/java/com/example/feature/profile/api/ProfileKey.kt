package com.example.feature.profile.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
enum class ProfileSubTab(val segment: String) {
    ACTIVITY("activity"),
    FOLLOWERS("followers"),
    RECRUITERS("recruiters"),
    ELSE("else");

    companion object {
        fun from(segment: String?): ProfileSubTab =
            entries.firstOrNull { it.segment.equals(segment, ignoreCase = true) } ?: ACTIVITY
    }
}

@Serializable
data class ProfileKey(
    val subTab: ProfileSubTab = ProfileSubTab.ACTIVITY,
) : NavKey
