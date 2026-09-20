package com.example.weather.domain.repository

import com.example.utils.Resource
import com.example.weather.domain.model.Coordinates
import com.example.weather.domain.model.Location
import com.example.weather.domain.model.Weather
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getPopularLocations(): Flow<Resource<List<Location>>>
    suspend fun getCoordinatesByCityName(name: String): Resource<Coordinates>
    suspend fun getWeatherByCoordinates(lat:Double, lon: Double): Resource<Weather>

}
