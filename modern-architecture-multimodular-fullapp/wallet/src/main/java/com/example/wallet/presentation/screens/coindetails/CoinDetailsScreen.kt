package com.example.wallet.presentation.screens.coindetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.designsystem.presentation.BodyLarge
import com.example.designsystem.presentation.BodySmall
import com.example.designsystem.presentation.ModernTopBar
import com.example.wallet.R

@Composable
fun CoinDetailsScreen(
    id: String,
    viewModel: CoinDetailsViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {

    LaunchedEffect(id) {
        viewModel.getCoinDetails(id)
    }

    val uiState by viewModel.uiState.collectAsState()

    Column(
        Modifier.fillMaxSize()
    ) {
        ModernTopBar(
            title = stringResource(R.string.wallet_detail),
            leadingIcon = Icons.AutoMirrored.Filled.ArrowBack,
            trailingIcon = null,
            onLeadingClick = onBackClick
        )
        Spacer(modifier = Modifier.height(16.dp))
        BodyLarge(id)
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
        if (uiState.coin != null){
            BodyLarge(text = uiState.coin.toString())
        }



    }

}
