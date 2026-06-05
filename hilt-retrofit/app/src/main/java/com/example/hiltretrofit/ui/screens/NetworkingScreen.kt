package com.example.hiltretrofit.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hiltretrofit.data.remote.RequestParam

@Composable
fun NetworkingScreen(
    paddingValues: PaddingValues,
    viewModel: NetworkingScreenViewModel = hiltViewModel()
){

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.padding(paddingValues).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // display 3 buttons with "android" , "kotlin" , "compose"
        for(feature in RequestParam.entries){
            Button(
                onClick = { viewModel.getFeaturesDescription(feature) }
            ) {
                Text(feature.formattedName)
            }
        }

        Spacer(Modifier.height(24.dp))

        val text = if(uiState.isEmpty()) "Loading..." else uiState.toString()
        Text(
            text = text
        )
    }
}