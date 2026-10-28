package com.example.multimodularcompleteretrofitemailapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.multimodularcompleteretrofitemailapp.ui.theme.MultimodularCompleteRetrofitEmailAppTheme

// 10:38
// https://6a63b9c2b30b52361e1a98d9.mockapi.io/api/v1/emailList
// https://6a63b9c2b30b52361e1a98d9.mockapi.io/api/v1/emailDetail


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MultimodularCompleteRetrofitEmailAppTheme {

            }
        }
    }
}

