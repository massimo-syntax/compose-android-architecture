package com.example.multimodularmodernarchitecturefullapp.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(val route: String){
    @Serializable
    data object HomeScreen : Screen("home")
    @Serializable
    data object ComponentsShowcase : Screen("components_showcase")
    @Serializable
    data object Wallet : Screen("wallet")
}