package com.example.wallet.data.repository

import com.example.database.local.datasource.LocalDataSource
import com.example.utils.Resource
import com.example.utils.logging.AppLogger
import com.example.wallet.data.remote.CoinApi
import com.example.wallet.data.remote.dto.toDomain
import com.example.wallet.domain.model.Coin
import com.example.wallet.domain.model.CoinDetail
import com.example.wallet.domain.model.toDomain
import com.example.wallet.domain.model.toEntity
import com.example.wallet.domain.repository.CoinRepository
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class CoinRepositoryImpl @Inject constructor(
    private val api: CoinApi,
    private val coinsDataSource: LocalDataSource
) : CoinRepository {

    override suspend fun getCoins(): Flow<Resource<List<Coin>>> = flow {
        emit(Resource.Loading())
        // first look in database
        val coins = getCoinsFromDb()
        coins?.let{
            emit(Resource.Success(it))
        }
        val newCoinsResult: Resource<List<Coin>> = getCoinsFromJsonApi()
        // feed database with new values and emit them
        if(newCoinsResult is Resource.Success){
            val result = newCoinsResult.data ?: emptyList()
            insertCoinsInDb(result)
            emit(Resource.Success(result))
        }
    }

    override suspend fun getCoinById(coinId: String): Flow<Resource<CoinDetail>> = flow {
        try {
            emit(Resource.Loading())
            val coinDetial = api.getCoinDetail(coinId).toDomain()
            emit(Resource.Success(coinDetial))
        } catch (e: HttpException){
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred" ) )
        } catch (e: IOException){
            emit(Resource.Error("Internet connection may be unavailable, server not reachable"))
        } catch (e: Exception){
            emit(Resource.Error("UNEXPECTED ERROR: " + e.message.toString()))
        }
    }

    private suspend fun getCoinsFromJsonApi(): Resource<List<Coin>> {
        return try {
            val coins = api.getCoins().map { it.toDomain() }
            Resource.Success(coins)
        } catch (e: HttpException) {
            Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
        } catch (e: IOException) {
            Resource.Error("Internet connection may be unavailable, server not reachable")
        } catch (e: Exception) {
            Resource.Error("UNEXPECTED ERROR: ${e.message}")
        }
    }

    private suspend fun getCoinsFromDb(): List<Coin>? =
        withContext(Dispatchers.IO){
            val coins = coinsDataSource.getCoinList()
            if(coins.isEmpty()) null
            else coins.map{ it.toDomain() }
        }

    private suspend fun insertCoinsInDb(coins: List<Coin>){
        withContext(Dispatchers.IO){
            coinsDataSource.insertCoinList(coins.map { it.toEntity() })
        }
    }


}