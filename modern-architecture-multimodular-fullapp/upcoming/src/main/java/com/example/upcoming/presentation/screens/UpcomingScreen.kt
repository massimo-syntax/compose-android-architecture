package com.example.upcoming.presentation.screens

import com.example.upcoming.R
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.designsystem.presentation.BodyLarge
import com.example.designsystem.presentation.BodySmall
import com.example.designsystem.presentation.ModernListItem
import com.example.designsystem.presentation.ModernTopBar

@Composable
fun UpcomingScreen(
    viewModel: UpcomingViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        Modifier.fillMaxSize()
    ) {
        ModernTopBar(
            title = stringResource(R.string.upcoming),
            leadingIcon = Icons.AutoMirrored.Filled.ArrowBack,
            trailingIcon = null,
            onLeadingClick = onBackClick
        )
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

        if (uiState.items.isNotEmpty()) {
            LazyColumn {
                items(items = uiState.items, key = { it.id }) { item ->
                    ModernListItem(
                        title = item.name,
                        subtitle = item.description ?: "",
                        leadingContent = {
                            BodySmall(item.date)
                        },
                        trailingContent = {
                            BodySmall(item.extra ?: "")
                        },
                        onClick = { /* Handle click if needed */ }
                    )
                }
            }
        }
    }
}
