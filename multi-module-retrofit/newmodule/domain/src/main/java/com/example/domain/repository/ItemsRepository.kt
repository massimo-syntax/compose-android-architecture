package com.example.domain.repository

import com.example.domain.model.Item

interface ItemsRepository {
    suspend fun getItems() : List<Item>
}