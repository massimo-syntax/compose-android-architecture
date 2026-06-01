package com.example.mock_interceptor_retrofit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mock_interceptor_retrofit.ui.screens.mainScreen.MainScreen
import com.example.mock_interceptor_retrofit.ui.theme.MockinterceptorretrofitTheme
import dagger.hilt.android.AndroidEntryPoint


// MyApplication class has to be on top of folder structure
// let say inside com.example.mock_interceptor_retrofit , not in further folders
// if it is in a folder called di, for example, the app crashes
// or i the manifest the <application ... android:name = "di.MyApplication"
// im not using ai yet for nothing :)
@AndroidEntryPoint
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

