package com.example.multimodularmodernarchitecturefullapp.presenatation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Adb
import androidx.compose.material.icons.filled.BuildCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.designsystem.presentation.BannerCard
import com.example.designsystem.presentation.ModernTopBar
import com.example.wallet.R as WalletR
import com.example.weather.R as WeatherR


@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToWallet: () -> Unit,
    onNavigateToUpcoming: () -> Unit,
    onNavigateToChangeAddress: () -> Unit,
    onNavigateToWeather: () -> Unit,
    ) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .statusBarsPadding()

    ) {
        // Modern Top Bar
        ModernTopBar(
            title = "Address",
            leadingIcon = Icons.Filled.Adb,
            trailingIcon = Icons.Default.BuildCircle,
            onLeadingClick = { /* Handle back */ },
            onTrailingClick = onNavigateToChangeAddress,
        )

        Column(
            Modifier
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            if(uiState.loading) CircularProgressIndicator(Modifier.size(100.dp))

            uiState.nextUpcoming?.let{
                BannerCard(
                    title = it.name,
                    subtitle = it.description ?: "",
                    imageUrl = "https://static.vecteezy.com/system/resources/thumbnails/047/664/958/small_2x/a-small-yellow-duck-on-a-transparent-background-png.png",
                    onClick = onNavigateToUpcoming
                )
            }

            BannerCard(
                title = stringResource(WalletR.string.wallet),
                subtitle = "Thats money, thought!",
                imageUrl = "https://i.pinimg.com/736x/4f/65/f9/4f65f997adebb58088cdf745fb3351e4.jpg",
                onClick = onNavigateToWallet
            )

            BannerCard(
                title = stringResource(WeatherR.string.weather),
                subtitle = "See the weather in your region",
                imageUrl = "https://cdn2.fptshop.com.vn/unsafe/1920x0/filters:format(webp):quality(75)/2023_11_26_638366159843367754_hinh-nen-bien.png",
                onClick = onNavigateToWeather
            )



        }


    }
}
