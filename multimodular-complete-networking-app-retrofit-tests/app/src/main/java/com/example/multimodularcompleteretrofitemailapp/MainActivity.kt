package com.example.multimodularcompleteretrofitemailapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.multimodularcompleteretrofitemailapp.ui.theme.MultimodularCompleteRetrofitEmailAppTheme
import com.example.navigation.AppNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MultimodularCompleteRetrofitEmailAppTheme {
                AppNavigation()
            }
        }
    }
}

