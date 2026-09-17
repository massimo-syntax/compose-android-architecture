package com.example.weather.presentation.screens.address

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.designsystem.presentation.BodyLarge
import com.example.designsystem.presentation.ModernTopBar
import com.example.weather.R

@Composable
fun SelectLocation(
    viewModel: LocationViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // Saves the current selected city as a string from the name when selected
    var selectedCity by remember { mutableStateOf<String?>(null) }

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
            // Row with Request Location (no functionality yet) between top bar and grid
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { /* No functionality yet */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Request Location")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (uiState.loading) {
                BodyLarge(
                    text = "loading...",
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(16.dp)
                )
            }

            if (uiState.error != null) {
                BodyLarge(
                    text = uiState.error.toString(),
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }

            if (uiState.items.isNotEmpty()) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.items, key = { it.name }) { item ->
                        val isSelected = selectedCity == item.name
                        
                        // Animates color when selected
                        val backgroundColor by animateColorAsState(
                            targetValue = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                            label = "backgroundColor"
                        )
                        val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                        
                        Surface(
                            onClick = {
                                // Toggled on click
                                selectedCity = if (isSelected) null else item.name
                            },
                            modifier = Modifier.aspectRatio(1f),
                            shape = RoundedCornerShape(8.dp),
                            color = backgroundColor,
                            border = BorderStroke(2.dp, borderColor)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                              ) {
                                Icon(
                                    imageVector = Icons.Default.LocationCity,
                                    contentDescription = null,
                                    tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = item.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
