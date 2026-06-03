package com.example.hiltretrofit.data.repository

import com.example.hiltretrofit.data.model.SimpleModel
import com.example.hiltretrofit.data.remote.DefaultApiService
import jakarta.inject.Inject

class NetworkRepositoryImpl @Inject constructor(
    private val apiService: DefaultApiService
) : NetworkRepository {
    override suspend fun getData(): List<SimpleModel> {
        return apiService.getData()
    }
}