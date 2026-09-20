package com.example.weather.domain.usecase

import com.example.utils.Resource
import com.example.weather.domain.model.Coordinates
import com.example.weather.domain.model.Weather
import com.example.weather.domain.repository.LocationRepository
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class GetWeatherByCityNameUseCase @Inject constructor(
    private val  repository: LocationRepository
) {
    suspend operator fun invoke(city: String): Resource<Weather>{
        val coordinates = repository.getCoordinatesByCityName(city)
        var lat = 0.0
        var lon = 0.0
        if(coordinates is Resource.Success){
            lat = coordinates.data?.lat ?: 0.0
            lon = coordinates.data?.lon ?: 0.0
        }else{
            return Resource.Error("coordinates for city $city not found")
        }
        // coordinates found
        val weather = repository.getWeatherByCoordinates(lat, lon)
        if(weather is Resource.Success){
            val weather = weather.data
            if(weather == null){
                return Resource.Error("weather by citynmame-> coordinates, resource.success is null")
            }else{
                return Resource.Success(weather)
            }
        }else{
            return Resource.Error("error fetching weather by coordinates")
        }
    }
}