package com.example.conventionpluginlibrary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.conventionpluginlibrary.ui.theme.ConventionPluginLibraryTheme
import com.example.ui.UiComposable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ConventionPluginLibraryTheme {
                UiComposable()
            }
        }
    }
}

