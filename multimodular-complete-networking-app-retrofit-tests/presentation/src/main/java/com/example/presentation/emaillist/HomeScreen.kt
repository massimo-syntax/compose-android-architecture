package com.example.presentation.emaillist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.networkresponse.error.getErrorMessage
import com.example.core.networkresponse.ext.formatTimestamp
import com.example.ui.components.ErrorScreen
import com.example.ui.components.HomeTopBar
import com.example.ui.components.MessageItem
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToDetails: (String) -> Unit
){

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // loading items not by viewModel init{}
    LaunchedEffect(uiState) {
        // prevent reloading when viewModel exists
        if(uiState !is EmailListState.Success){
            viewModel.event(EmailListEvent.LoadEmailList)
        }
    }

    // observe navigation
    LaunchedEffect(Unit){
        viewModel.effect.collectLatest {
            when(it){
                is EmailListEffect.NavigateToEmailDetails -> {
                    onNavigateToDetails(it.model.id)
                }
            }
        }
    }

    Column(
        modifier = Modifier.statusBarsPadding()
    ){
        HomeTopBar(
            title = "Title Home",
            profileImageUrl = "https://i.pravatar.cc/250?img=60"
        )

        when(uiState){
            is EmailListState.Loading -> CircularProgressIndicator(Modifier.size(64.dp))
            is EmailListState.Error -> {
                val message = (uiState as EmailListState.Error).error.getErrorMessage()
                ErrorScreen(message)
            }
            is EmailListState.Success -> {
                val items = (uiState as EmailListState.Success).data
                LazyColumn(
                    Modifier.padding(16.dp)
                ) {
                    items ( items = items, key = { it.id }){
                        MessageItem(
                            sender = it.from,
                            title = it.title,
                            snippet = "${it.id}${it.favorite}${it.title}",
                            timestamp = formatTimestamp(it.timestamp),
                            imageUrl = it.image,
                            isFavorite = it.favorite,
                            onItemClick = {
                                viewModel.event(EmailListEvent.EmailClicked(it))
                            },
                            onFavoriteClick = {}
                        )
                    }
                }
            }
        }

    }

}