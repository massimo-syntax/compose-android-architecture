package com.example.navigation

import com.example.feature.details.api.EmailDetailsKey
import com.example.navigation.api.MainKey
import com.example.navigation.api.MainTab
import org.junit.Assert.assertEquals
import org.junit.Test

class DeepLinkPathTest {

    @Test
    fun testEmailDetailsDeepLink() {
        val path = DeepLinkPath("emaillist/emaildetails/10")
        val expected = listOf(
            MainKey(MainTab.EMAILLIST, null),
            EmailDetailsKey("10")
        )
        assertEquals(expected, path.stack)
    }

    @Test
    fun testEmailDetailsDeepLinkWithScheme() {
        val path = DeepLinkPath("deeplink://emaillist/emaildetails/10")
        val expected = listOf(
            MainKey(MainTab.EMAILLIST, null),
            EmailDetailsKey("10")
        )
        assertEquals(expected, path.stack)
    }

    @Test
    fun testProfileSubTabDeepLink() {
        val path = DeepLinkPath("profile/followers")
        val expected = listOf(
            MainKey(MainTab.PROFILE, subTab = "followers")
        )
        assertEquals(expected, path.stack)
    }

    @Test
    fun testSettingsDeepLink() {
        val path = DeepLinkPath("settings")
        val expected = listOf(
            MainKey(MainTab.SETTINGS, subTab = null)
        )
        assertEquals(expected, path.stack)
    }

    @Test
    fun testUnknownOrNullDeepLinkFallback() {
        val pathNull = DeepLinkPath(null)
        assertEquals(listOf(MainKey(MainTab.EMAILLIST, null)), pathNull.stack)

        val pathUnknown = DeepLinkPath("invalid/path/123")
        assertEquals(listOf(MainKey(MainTab.EMAILLIST, null)), pathUnknown.stack)
    }
}
