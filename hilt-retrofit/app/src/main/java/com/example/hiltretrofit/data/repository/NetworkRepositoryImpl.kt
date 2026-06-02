package com.example.hiltretrofit.data.repository

import com.example.hiltretrofit.data.model.SimpleModel
import jakarta.inject.Inject

class NetworkRepositoryImpl @Inject constructor() : NetworkRepository {
    override suspend fun getData(): List<SimpleModel> {
        return listOf(SimpleModel("0", "text"))
    }
}