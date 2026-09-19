package com.example.weather.presentation.screens.weather

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.designsystem.presentation.BodyLarge
import com.example.designsystem.presentation.ModernListItem
import com.example.designsystem.presentation.ModernTopBar
import com.example.weather.R

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            ModernTopBar(
                title = stringResource(R.string.weather),
                leadingIcon = Icons.AutoMirrored.Filled.ArrowBack,
                trailingIcon = null,
                onLeadingClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.loading) {
                BodyLarge(
                    text = "loading...",
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            if (uiState.error != null) {
                BodyLarge(
                    text = uiState.error.toString(),
                    color = MaterialTheme.colorScheme.error
                )
            }

            if (!uiState.city.isNullOrEmpty()) {
                BodyLarge(uiState.city ?: "it was null")
            }
            if(uiState.coordinates != null){
                val lat = uiState.coordinates?.lat ?: "it was null"
                val lon = uiState.coordinates?.lon ?: "it was null"
                BodyLarge("Lat: $lat")
                BodyLarge("Lon: $lon")
            }
        }
    }
}
