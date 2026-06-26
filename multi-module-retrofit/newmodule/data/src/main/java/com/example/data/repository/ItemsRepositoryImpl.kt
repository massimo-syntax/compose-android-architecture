package com.example.data.repository

import com.example.data.api.MyApiService
import com.example.domain.model.Item
import com.example.domain.repository.ItemsRepository
import jakarta.inject.Inject

class ItemsRepositoryImpl @Inject constructor(
    private val apiService: MyApiService
) : ItemsRepository {
    override suspend fun getItems() : List<Item> {
      return apiService.getData()
    }

    /*
    override suspend fun getItems(): List<Item> {
        val items = apiService
        return listOf(Item("$apiService","hello","anus"))
    }
    */
}