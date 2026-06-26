package com.example.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ListItem(
    id:String,
    text: String
){
    Row {
        Text("id: $id - $text")
    }
}