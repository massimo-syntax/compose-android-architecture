package com.example.flowsfromgoogledocs.data

import com.example.flowsfromgoogledocs.data.model.New
import com.example.flowsfromgoogledocs.data.source.datasource.NewsRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach


object Cache{
    var news: List<New> = emptyList()
    val lastCachedNews = { news }
}

class NewsRepository(
    private val newsRemoteDataSource: NewsRemoteDataSource,
    private val defaultDispatcher: CoroutineDispatcher
) {
    /**
     * Returns the favorite latest news applying transformations on the flow.
     * These operations are lazy and don't trigger the flow. They just transform
     * the current value emitted by the flow at that point in time.
     */
    val favoriteLatestNews: Flow<List<New>> =
        newsRemoteDataSource.latestNews
            // Intermediate operation to filter the list of favorite topics
            .map { news ->
                news.filter { it.id % 2 == 1 }
            }// Executes on the default dispatcher

            // Intermediate operation to save the latest news in the cache
            .onEach { news ->
                Cache.news = news
            }// Executes on the default dispatcher
            // flowOn affects the upstream flow ↑
            .flowOn(defaultDispatcher)

            // the downstream flow ↓ is not affected
            .catch { exception ->
                    emit(Cache.lastCachedNews())
            }// Executes in the consumer's context


}
