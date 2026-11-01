package com.example.multimodularcompleteretrofitemailapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.multimodularcompleteretrofitemailapp.ui.theme.MultimodularCompleteRetrofitEmailAppTheme
import com.example.presentation.emaillist.HomeScreen
import dagger.hilt.android.AndroidEntryPoint

// 10:38
// https://6a63b9c2b30b52361e1a98d9.mockapi.io/api/v1/emailList
// https://6a63b9c2b30b52361e1a98d9.mockapi.io/api/v1/emailDetail


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MultimodularCompleteRetrofitEmailAppTheme {
                HomeScreen(
                    onNavigateToDetails = {
                        Toast.makeText(this, "click", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}

