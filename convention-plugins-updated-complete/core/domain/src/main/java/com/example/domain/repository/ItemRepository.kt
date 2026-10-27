package com.example.domain.repository

import com.example.domain.model.Item

interface ItemRepository {
    suspend fun getItems(): List<Item>
}
