package com.example.weather.domain.repository

import com.example.utils.Resource
import com.example.weather.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getPopularLocations(): Flow<Resource<List<Location>>>
    fun selectLocation(location: String?)
    fun requestLocation()
}
