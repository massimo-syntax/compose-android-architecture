package com.example.multimodularmodernarchitecturefullapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.multimodularmodernarchitecturefullapp.navigation.AppNavGraph
import com.example.multimodularmodernarchitecturefullapp.presenatation.screens.home.HomeScreen
import com.example.multimodularmodernarchitecturefullapp.ui.theme.MultimodularModernArchitectureFullAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MultimodularModernArchitectureFullAppTheme {
                AppNavGraph()
            }
        }
    }
}

