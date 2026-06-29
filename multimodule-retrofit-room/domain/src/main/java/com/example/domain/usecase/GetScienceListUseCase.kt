package com.example.domain.usecase

import com.example.domain.model.EventualNetworkFeedback
import com.example.domain.model.Science
import com.example.domain.repository.ScienceRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetScienceListUseCase @Inject constructor(
    private val repository: ScienceRepository
) {

    /*
    operator fun invoke(page: Int = 1): Flow<List<Movie>> {
        return repository.getPopularMovies()
    }
     */
    fun observeDb(): Flow<List<Science>> = repository.observeScienceDb()

    operator suspend fun invoke(): EventualNetworkFeedback {
        return repository.requestAndSaveScienceList()
    }

}
