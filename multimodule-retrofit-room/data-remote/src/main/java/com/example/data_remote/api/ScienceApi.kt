package com.example.data_remote.api

import com.example.data_remote.dto.ScienceDto
import retrofit2.http.GET

interface ScienceApi {

    @GET("categories/science")
    suspend fun getScienceList(): List<ScienceDto>
}