package com.example.flowsfromgoogledocs.ui.mainScreen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MainScreen(
    //implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.5")
    viewModel: MainScreenViewModel = viewModel(),
    ) {

        val newsState by viewModel.news.collectAsStateWithLifecycle()
        Text(newsState.toString())


}