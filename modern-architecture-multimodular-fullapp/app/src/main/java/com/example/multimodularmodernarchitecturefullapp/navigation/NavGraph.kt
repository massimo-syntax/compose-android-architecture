package com.example.multimodularmodernarchitecturefullapp.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
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
import com.example.wallet.presentation.screens.home.WalletHomeScreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.toRoute
import com.example.wallet.presentation.screens.coindetails.CoinDetailsScreen
import com.example.wallet.presentation.screens.home.WalletViewModel
import com.example.upcoming.presentation.screens.UpcomingScreen
import com.example.upcoming.presentation.screens.UpcomingViewModel

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
            composable<Screen.HomeScreen>(

                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(500)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        tween(500)
                    )
                }
            ) {
                HomeScreen(
                    onNavigateToWallet = {
                        navController.navigate(Screen.Wallet)
                    },
                    onNavigateToUpcoming = {
                        navController.navigate(Screen.Upcoming)
                    }

                )
            }
            composable<Screen.Upcoming>(
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(500)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        tween(500)
                    )
                }
            ) {
                val viewModel: UpcomingViewModel = hiltViewModel()
                UpcomingScreen(
                    viewModel = viewModel,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
            composable<Screen.Wallet>(

                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(500)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        tween(500)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(500)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        tween(500)
                    )
                },
            ) {
                val viewModel: WalletViewModel = hiltViewModel()
                WalletHomeScreen(
                    viewModel = viewModel,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onNavigateToDetails = { coinId ->
                        navController.navigate( Screen.WalletDetails(coinId))
                    }
                )
            }
            composable<Screen.WalletDetails>(
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(500)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        tween(500)
                    )
                }
            ){ backStackEntry ->
                val coin = backStackEntry.toRoute<Screen.WalletDetails>()
                CoinDetailsScreen(
                    id = coin.coinId,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }

}