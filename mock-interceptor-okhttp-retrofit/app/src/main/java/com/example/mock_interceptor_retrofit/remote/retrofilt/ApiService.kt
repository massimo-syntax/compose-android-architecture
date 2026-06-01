package com.example.mock_interceptor_retrofit.remote.retrofilt

import retrofit2.http.GET

interface MockApiService {
    @GET("data")
    suspend fun getData(): List<ResponseModel>

    @GET("other")
    suspend fun getOther(): List<ResponseModel>

}