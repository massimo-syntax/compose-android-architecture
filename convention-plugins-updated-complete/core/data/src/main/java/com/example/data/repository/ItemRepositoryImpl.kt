package com.example.data.repository

import com.example.domain.model.Item
import com.example.domain.repository.ItemRepository
import javax.inject.Inject

class ItemRepositoryImpl @Inject constructor() : ItemRepository {
    override suspend fun getItems(): List<Item> {
        return listOf(
            Item(id = "1", name = "First Item"),
            Item(id = "2", name = "Second Item"),
            Item(id = "3", name = "Third Item")
        )
    }
}
