package com.example.weather.data.remote

import com.example.utils.constants.Constants
import com.example.weather.data.remote.dto.citynamecoordinates.CityNameCoordinates
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("/api/v1/weather/geocode")
    suspend fun getCoordinatesByCityName(
        @Query("city") name: String,
        @Query("apiKey") apiKey: String = Constants.WEATHER_API_KEY
    ) : CityNameCoordinates


}