package com.example.data_remote.dataSource

import com.example.data_remote.api.ScienceApi
import com.example.data_remote.apiResult.ApiResult
import com.example.data_remote.apiResult.trySafeApiCall
import com.example.data_remote.dto.ScienceDto
import jakarta.inject.Inject

class ScienceRemoteDataSource @Inject constructor(
    private val api: ScienceApi
) {
    suspend fun getScienceList(): ApiResult<List<ScienceDto>> =
        trySafeApiCall{ api.getScienceList() }

}

