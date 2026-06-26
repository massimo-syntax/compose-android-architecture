package com.example.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun NewScreen(){
    Column(
        Modifier.padding(24.dp)
    ) {
        Text("Hello from another module")
    }
}