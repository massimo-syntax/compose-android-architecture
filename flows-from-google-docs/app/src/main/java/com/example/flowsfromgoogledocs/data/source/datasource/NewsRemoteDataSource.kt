package com.example.flowsfromgoogledocs.data.source.datasource

import com.example.flowsfromgoogledocs.data.model.New
import com.example.flowsfromgoogledocs.data.source.remote.NewsApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class NewsRemoteDataSource(
    private val newsApi: NewsApi,
    private val refreshIntervalMs: Long = 5000,
    private val ioDispatcher: CoroutineDispatcher
) {
    val latestNews: Flow<List<New>> = flow {
        // Executes on the IO dispatcher
        while(true) {
            val latestNews = newsApi.fetchLatestNews()
            emit(latestNews) // Emits the result of the request to the flow
            delay(refreshIntervalMs) // Suspends the coroutine for some time
        }
    }.flowOn(ioDispatcher)
}