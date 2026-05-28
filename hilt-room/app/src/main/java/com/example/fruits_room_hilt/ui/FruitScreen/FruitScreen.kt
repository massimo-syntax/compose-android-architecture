package com.example.fruits_room_hilt.ui.FruitScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.fruits_room_hilt.data.model.Fruit

@Composable
fun FruitScreen(
    viewModel: FruitViewModel = hiltViewModel()
) {
    val fruitUiState by viewModel.fruitUiState.collectAsState()
    var fruitName by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                TextField(
                    value = fruitName,
                    onValueChange = { fruitName = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Enter fruit name") }
                )
                Button(
                    onClick = {
                        if (fruitName.isNotBlank()) {
                            viewModel.saveFruit(Fruit(name = fruitName))
                            fruitName = ""
                        }
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Add")
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                items(fruitUiState.fruits) { fruit ->
                    Text(
                        text = fruit.run{"$id - $name"},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}

