package com.example.conventionpluginsconfigurationlibs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.runtime.Composable

import com.example.conventionpluginsconfigurationlibs.ui.theme.ConventionPluginsConfigurationLibsTheme
import com.example.gradle_plugins_jurney.ui.Ui

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ConventionPluginsConfigurationLibsTheme {
                Ui()
            }
        }
    }
}

