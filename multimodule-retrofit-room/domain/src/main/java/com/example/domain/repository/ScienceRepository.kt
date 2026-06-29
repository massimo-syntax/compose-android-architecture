package com.example.domain.repository

import com.example.domain.model.EventualNetworkFeedback
import com.example.domain.model.Science
import kotlinx.coroutines.flow.Flow

interface ScienceRepository {

    fun observeScienceDb(): Flow<List<Science>>
    suspend fun requestAndSaveScienceList(): EventualNetworkFeedback

}