package com.example.weather.domain.repository

import com.example.weather.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getPopularLocations(): Flow<List<Location>>
    fun requestLocation()
}
