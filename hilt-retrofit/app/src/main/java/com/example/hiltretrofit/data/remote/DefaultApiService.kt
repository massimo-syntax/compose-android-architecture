package com.example.hiltretrofit.data.remote

import com.example.hiltretrofit.data.model.SimpleModel
import retrofit2.http.GET

interface DefaultApiService {
    @GET("default")
    suspend fun getData(): List<SimpleModel>
}