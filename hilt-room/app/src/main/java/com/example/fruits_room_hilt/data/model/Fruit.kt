package com.example.fruits_room_hilt.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Fruit(
    @PrimaryKey(autoGenerate = true) val id:Long = 0,
    val name:String,
)
