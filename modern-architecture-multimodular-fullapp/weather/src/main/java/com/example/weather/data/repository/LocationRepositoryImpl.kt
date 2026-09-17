package com.example.weather.data.repository

import com.example.weather.domain.model.Location
import com.example.weather.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepositoryImpl @Inject constructor() : LocationRepository {

    override fun getPopularLocations(): Flow<List<Location>> {
        return flowOf(
            listOf(
                Location("Lisbon", 387223, -91393),
                Location("Madrid", 404168, -37038),
                Location("Paris", 488566, 23522),
                Location("Amsterdam", 523676, 49041),
                Location("Berlin", 525200, 134050),
                Location("Rome", 419028, 124964),
                Location("Vienna", 482082, 163738),
                Location("Prague", 500755, 144378),
                Location("Warsaw", 522297, 210122),
                Location("Budapest", 474979, 190402),
                Location("Bucharest", 444268, 261025),
                Location("Chisinau", 470105, 288638)
            )
        )
    }

    override fun requestLocation() {
        // Not implemented yet
    }
}
