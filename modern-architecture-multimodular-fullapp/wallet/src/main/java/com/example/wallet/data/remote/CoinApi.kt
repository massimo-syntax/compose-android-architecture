package com.example.wallet.data.remote

import com.example.wallet.data.remote.dto.CoinDetailDto
import com.example.wallet.data.remote.dto.CoinDto
import retrofit2.http.GET
import retrofit2.http.Path

interface CoinApi {

    @GET("/v1/coins")
    suspend fun getCoins() : List<CoinDto>

    @GET("/vi/coins/{id}")
    suspend fun getCoinDetail(@Path("id") coinId: String) : CoinDetailDto


}