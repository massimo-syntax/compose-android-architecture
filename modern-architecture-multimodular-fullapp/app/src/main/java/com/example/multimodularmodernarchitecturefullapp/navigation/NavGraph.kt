package com.example.multimodularmodernarchitecturefullapp.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.multimodularmodernarchitecturefullapp.presenatation.screens.ModernScreen
import com.example.multimodularmodernarchitecturefullapp.presenatation.screens.home.HomeScreen
import com.example.wallet.presentation.home.WalletHomeScreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.wallet.presentation.home.WalletViewModel

@Composable
fun AppNavGraph(){
    val navController = rememberNavController()
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ){
        NavHost(
            navController,
            startDestination = Screen.HomeScreen
        ) {
            composable<Screen.ComponentsShowcase>{
                ModernScreen()
            }
            composable<Screen.HomeScreen> {
                HomeScreen(
                    onNavigateToWallet = {
                        navController.navigate(Screen.Wallet)
                    }
                )
            }
            composable<Screen.Wallet> {
                val viewModel: WalletViewModel = hiltViewModel()
                WalletHomeScreen(
                    viewModel = viewModel,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }

}