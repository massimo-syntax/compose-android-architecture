package com.example.addingonemodule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.addingonemodule.ui.theme.AddingOneModuleTheme
import com.example.ui.NewScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AddingOneModuleTheme {
                NewScreen()
            }
        }
    }
}


