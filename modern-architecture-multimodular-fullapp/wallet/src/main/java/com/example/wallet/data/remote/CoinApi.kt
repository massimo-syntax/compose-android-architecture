package com.example.wallet.data.remote

import com.example.wallet.data.remote.dto.CoinDetailDto
import com.example.wallet.data.remote.dto.CoinDto
import com.example.wallet.data.remote.dto.cointicker.CoinTickerDto
import retrofit2.http.GET
import retrofit2.http.Path

interface CoinApi {

    @GET("/v1/coins")
    suspend fun getCoins() : List<CoinDto>

    @GET("/v1/coins/{id}")
    suspend fun getCoinDetail(@Path("id") coinId: String) : CoinDetailDto

    @GET("/v1/tickers/{id}")
    suspend fun getCoinTicker(@Path("id") coinId: String) : CoinTickerDto

}