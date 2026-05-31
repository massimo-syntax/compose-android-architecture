package com.example.mock_interceptor_retrofit.remote.retrofilt

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.getValue

val mockRetrofit: ApiService by lazy {
    val client = OkHttpClient.Builder()
        .addInterceptor(MockInterceptor())
        .build()
    Retrofit.Builder()
        .baseUrl("https://mock.api/") // dummy URL
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
}