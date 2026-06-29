package com.example.presentation.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ListingScreen(
    viewModel: ListingViewModel = hiltViewModel()
){

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val eventualNetworkFeedback by remember { viewModel.networkError }
    Column(
        Modifier.padding(24.dp)
    ){
        if(uiState.loading){
            Text("L O A D I N G")
            return@Column
        }
        LazyColumn {
            items(items = uiState.data, key = {it.id}){
                Text("concept: ${it.concept}, field: ${it.field}")
            }
        }
    }

    if(!eventualNetworkFeedback.success)
        Toast.makeText(LocalContext.current,eventualNetworkFeedback.message,Toast.LENGTH_SHORT).show()



}