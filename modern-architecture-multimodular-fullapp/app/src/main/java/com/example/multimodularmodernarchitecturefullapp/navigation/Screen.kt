package com.example.multimodularmodernarchitecturefullapp.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(val route: String){
    @Serializable
    data object HomeScreen : Screen(Route.HOME.name)
    @Serializable
    data object ComponentsShowcase : Screen("just testing screen")
    @Serializable
    data object Wallet : Screen(Route.WALLET_HOME.name)
    @Serializable
    data object Upcoming : Screen(Route.UPCOMING.name)

    @Serializable
    data class WalletDetails(val coinId: String) : Screen(Route.WALLET_DETAILS.name)

    @Serializable
    data object Weather : Screen(Route.WEATHER.name)

    @Serializable
    data object SelectLocation : Screen(Route.SELECT_LOCATION.name)

}

enum class Route{
    HOME,
    WALLET_HOME,
    WALLET_DETAILS,
    UPCOMING,
    WEATHER,
    SELECT_LOCATION,
}