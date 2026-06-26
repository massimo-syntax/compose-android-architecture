package com.example.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@Composable
fun NewModuleScreen(
    viewModel: NewModuleViewModel = hiltViewModel()
){
    val data by viewModel.uiState.collectAsStateWithLifecycle()
    Column(
        Modifier.padding(24.dp)
    ) {
        LazyColumn {
            items(items = data, key = {it.id}){
                ListItem("${it.id}",it.title)
            }
        }
    }
}


