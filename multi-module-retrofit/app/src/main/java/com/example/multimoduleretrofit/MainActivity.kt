package com.example.multimoduleretrofit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.multimoduleretrofit.ui.theme.MultiModuleRetrofitTheme
import com.example.ui.NewModuleScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MultiModuleRetrofitTheme {
                NewModuleScreen()
            }
        }
    }
}
