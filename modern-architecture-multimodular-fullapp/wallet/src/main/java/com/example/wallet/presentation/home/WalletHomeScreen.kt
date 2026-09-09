package com.example.wallet.presentation.home

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.designsystem.presentation.BodyLarge
import com.example.designsystem.presentation.BodySmall
import com.example.designsystem.presentation.ModernListItem
import com.example.designsystem.presentation.ModernTopBar
import com.example.wallet.R

@Composable
fun WalletHomeScreen(
      viewModel: WalletViewModel,
      onBackClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        Modifier.fillMaxSize()
    ) {
        ModernTopBar(
            title = stringResource(R.string.wallet),
            leadingIcon = Icons.AutoMirrored.Filled.ArrowBack,
            trailingIcon = null,
            onLeadingClick = onBackClick
        )
        Spacer(modifier = Modifier.height(16.dp))

        if(uiState.isLoading){
            BodyLarge(
                text = "loading...",
                color = MaterialTheme.colorScheme.secondary)
        }

        if(uiState.error != null){
            BodyLarge(
                text = uiState.error.toString(),
                color = MaterialTheme.colorScheme.error
            )
            BodySmall(
                text = uiState.error.toString(),
                color = MaterialTheme.colorScheme.error
            )
        }

        if(uiState.coins.isNotEmpty()){
            LazyColumn{
                items(items = uiState.coins, key = { it.id } ){
                    ModernListItem(
                        title = it.name,
                        subtitle = it.symbol,
                        leadingContent = {
                            BodySmall(it.symbol)
                        },
                        trailingContent = {
                            BodySmall(it.rank.toString())
                        }
                    )
                }
            }
        }


    }

}
