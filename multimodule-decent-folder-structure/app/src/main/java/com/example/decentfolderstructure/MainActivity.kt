package com.example.decentfolderstructure

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.decentfolderstructure.ui.theme.DecentFolderStructureTheme
import com.example.ui.TodoScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DecentFolderStructureTheme {
                TodoScreen()
            }
        }
    }
}
