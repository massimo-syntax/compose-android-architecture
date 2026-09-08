package com.example.multimodularmodernarchitecturefullapp.presenatation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Adb
import androidx.compose.material.icons.filled.BuildCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.designsystem.presentation.BannerCard
import com.example.designsystem.presentation.ModernTopBar
import com.example.wallet.R

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToWallet: () -> Unit
) {
    HomeContent(
        onNavigateToWallet = onNavigateToWallet
    )
}

@Composable
fun HomeContent(
    onNavigateToWallet: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .statusBarsPadding()
    ) {
        // Modern Top Bar
        ModernTopBar(
            title = "Modern Components",
            leadingIcon = Icons.Filled.Adb,
            trailingIcon = Icons.Default.BuildCircle,
            onLeadingClick = { /* Handle back */ },
            onTrailingClick = { /* Handle menu */ }
        )
        Column(
            Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            BannerCard(
                title = stringResource(R.string.wallet),
                subtitle = "Modern & Elegant",
                imageUrl = "https://i.pinimg.com/736x/4f/65/f9/4f65f997adebb58088cdf745fb3351e4.jpg",
                onClick = onNavigateToWallet
            )

        }


    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeContent()
}