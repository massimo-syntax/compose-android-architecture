package com.example.navigation

import androidx.navigation3.runtime.NavKey
import com.example.feature.details.api.EmailDetailsKey
import com.example.navigation.api.MainKey
import com.example.navigation.api.MainTab
import java.net.URI

class DeepLinkPath(link: String?) {

    /** Full back stack. Always starts with MainKey. */
    val stack: List<NavKey>

    init {
        val segments = parseSegments(link)
        val rest = segments.drop(1)

        stack = when (segments.firstOrNull()) {
            MainTab.EMAILLIST.segment -> parseEmailList(rest)
            MainTab.PROFILE.segment -> parseProfile(rest)
            MainTab.SETTINGS.segment -> parseSettings()
            else -> listOf(MainKey(MainTab.EMAILLIST))
        }
    }

    private fun parseEmailList(rest: List<String>): List<NavKey> {
        val main = MainKey(MainTab.EMAILLIST)
        val detailSegment = rest.firstOrNull()
        val id = rest.getOrNull(1)
        return if ((detailSegment == "emaildetails" || detailSegment == "detail") && !id.isNullOrBlank()) {
            listOf(main, EmailDetailsKey(id))
        } else {
            listOf(main)
        }
    }

    private fun parseProfile(rest: List<String>): List<NavKey> =
        listOf(MainKey(MainTab.PROFILE, subTab = rest.firstOrNull()))

    private fun parseSettings(): List<NavKey> =
        listOf(MainKey(MainTab.SETTINGS))

    private fun parseSegments(link: String?): List<String> {
        if (link.isNullOrBlank()) return emptyList()
        val formattedLink = if (link.contains("://")) link else "deeplink://$link"
        val uri = runCatching { URI(formattedLink) }.getOrNull() ?: return emptyList()
        val path = uri.path.orEmpty().split("/").filter { it.isNotBlank() }
        return (listOfNotNull(uri.host) + path).map { it.lowercase() }
    }
}
