package com.example.conventionpluginsupdatedcomplete

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.conventionpluginsupdatedcomplete.ui.theme.ConventionPluginsUpdatedCompleteTheme
import com.example.myfeature.MyFeatureScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ConventionPluginsUpdatedCompleteTheme {
                MyFeatureScreen()
            }
        }
    }
}
