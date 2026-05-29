package com.example.flowsfromgoogledocs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.flowsfromgoogledocs.ui.mainScreen.MainScreen
import com.example.flowsfromgoogledocs.ui.theme.FlowsFromGoogleDocsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlowsFromGoogleDocsTheme {
                Scaffold{
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(it),
                        contentAlignment = Alignment.Center,
                    ) {
                        MainScreen()
                    }
                }

            }
        }
    }
}

// @TODO
// Executing in a different CoroutineContext
