package com.example.mutlimoduleretrofitroom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mutlimoduleretrofitroom.ui.theme.MutlimoduleRetrofitRoomTheme
import com.example.presentation.ui.ListingScreen
import dagger.hilt.android.AndroidEntryPoint

// https://api.mydummyapi.com/categories/science

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MutlimoduleRetrofitRoomTheme {
                ListingScreen()
            }
        }
    }
}

