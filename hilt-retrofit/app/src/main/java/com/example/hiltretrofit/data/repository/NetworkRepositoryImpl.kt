package com.example.hiltretrofit.data.repository

import com.example.hiltretrofit.data.model.SimpleModel
import com.example.hiltretrofit.data.remote.DefaultApiService
import com.example.hiltretrofit.data.remote.MockApiService
import com.example.hiltretrofit.data.remote.RequestParam
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlin.random.Random

class NetworkRepositoryImpl @Inject constructor(
    private val defaultApiService: DefaultApiService,
    private val mockApiService: MockApiService
) : NetworkRepository {

    override suspend fun getDefaultData(): List<SimpleModel> {
        return defaultApiService.getData()
    }

    override suspend fun getDetailsData(param: RequestParam): List<SimpleModel> {
        // mimic network delay
        val delay = Random.nextLong(2000)
        delay(delay)
        return mockApiService.getData(param.formattedName)
    }
}