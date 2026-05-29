package com.example.flowsfromgoogledocs.data.source.remote

import com.example.flowsfromgoogledocs.data.model.New
import com.example.flowsfromgoogledocs.data.source.remote.NewsApi
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.time.ExperimentalTime

object MyServer : NewsApi {

    override suspend fun fetchLatestNews(): List<New> {
        delay(200)
        return generateSampleNews()
    }

    @OptIn(ExperimentalTime::class)
    private fun generateSampleNews(count: Int = 50): List<New> {
        val rnd = Random.Default
        val now = System.currentTimeMillis()
        return List(count) { i ->
            New(
                id = rnd.nextInt(0, Int.MAX_VALUE),
                title = "Title #${i + 1}",
                body = "Body for item ${i + 1}",
                datetime = now - rnd.nextLong(0, 1000L * 60 * 60 * 24 * 365) // within last year
            )
        }
    }

}