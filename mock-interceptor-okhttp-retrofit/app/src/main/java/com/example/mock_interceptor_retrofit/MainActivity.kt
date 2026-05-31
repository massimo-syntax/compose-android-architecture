package com.example.mock_interceptor_retrofit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mock_interceptor_retrofit.ui.screens.mainScreen.MainScreen
import com.example.mock_interceptor_retrofit.ui.theme.MockinterceptorretrofitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MockinterceptorretrofitTheme {
                MainScreen()
            }
        }
    }
}

