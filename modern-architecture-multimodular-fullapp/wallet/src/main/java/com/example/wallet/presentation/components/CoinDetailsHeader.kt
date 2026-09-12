package com.example.wallet.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.designsystem.presentation.BodyLarge
import com.example.designsystem.presentation.BodySmall
import com.example.designsystem.presentation.TitleLarge
import com.example.wallet.domain.model.CoinDetail

@Composable
fun CoinDetailsHeader(coin: CoinDetail){
    Column(
        Modifier.wrapContentSize().padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = coin.logo,
                contentDescription = coin.name,
                modifier = Modifier.size(96.dp),
                contentScale = ContentScale.FillBounds,
            )
            Spacer(Modifier.height(36.dp))
            TitleLarge(coin.name)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(36.dp)) {
            BodyLarge(coin.symbol)
            BodyLarge(coin.rank.toString())
        }
        BodySmall(coin.description)
    }
}