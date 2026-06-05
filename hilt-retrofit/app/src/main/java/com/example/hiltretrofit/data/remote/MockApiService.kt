package com.example.hiltretrofit.data.remote

import com.example.hiltretrofit.data.model.SimpleModel
import retrofit2.http.GET
import retrofit2.http.Path

interface MockApiService {
    @GET("mock/{selected}")
    suspend fun getData(
        @Path("selected") selected: String
    ): List<SimpleModel>
}