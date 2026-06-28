package com.example.domain.usecase

import com.example.domain.model.Science
import com.example.domain.repository.ScienceRepository
import jakarta.inject.Inject

class GetScienceListUseCase @Inject constructor(
    private val repository: ScienceRepository
) {

    /*
    operator fun invoke(page: Int = 1): Flow<List<Movie>> {
        return repository.getPopularMovies()
    }
     */
    operator suspend fun invoke(): List<Science> =
        repository.getScienceList()

}
