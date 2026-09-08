package com.example.wallet.presentation.screens.wallet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.designsystem.presentation.ModernTopBar
import com.example.wallet.R

@Composable
fun WalletScreen(
    viewModel: WalletViewModel,
    onBackClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        ModernTopBar(
            title = stringResource(R.string.wallet),
            leadingIcon = Icons.AutoMirrored.Filled.ArrowBack,
            trailingIcon = null,
            onLeadingClick = onBackClick
        )
        Text(
            text = "Balance: ${uiState.balance}",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
