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
    data class WalletDetails(val coinId: String) : Screen(Route.WALLET_DETAILS.name)

}

enum class Route{
    HOME,
    WALLET_HOME,
    WALLET_DETAILS
}