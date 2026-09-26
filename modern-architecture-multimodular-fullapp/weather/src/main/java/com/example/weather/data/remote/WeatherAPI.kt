package com.example.weather.data.remote

import com.example.utils.constants.Constants
import com.example.weather.data.remote.dto.citynamecoordinates.CityNameCoordinatesDto
import com.example.weather.data.remote.dto.weather.WeatherDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("/api/v1/weather/geocode")
    suspend fun getCoordinatesByCityName(
        @Query("city") name: String,
        @Query("apiKey") apiKey: String = Constants.WEATHER_API_KEY
    ) : CityNameCoordinatesDto

    @GET("/api/v1/weather/current?")
    suspend fun getWeatherByCoordinates(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("apiKey") apiKey: String = Constants.WEATHER_API_KEY
    ) : WeatherDto

}