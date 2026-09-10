package com.example.wallet.data.repository

import com.example.utils.Resource
import com.example.wallet.data.remote.CoinApi
import com.example.wallet.data.remote.dto.toDomain
import com.example.wallet.domain.model.Coin
import com.example.wallet.domain.model.CoinDetail
import com.example.wallet.domain.repository.CoinRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class CoinRepositoryImpl @Inject constructor(
    private val api: CoinApi
) : CoinRepository {


    override suspend fun getCoins(): Flow<Resource<List<Coin>>> = flow{
        try {
            emit(Resource.Loading())
            val coins = api.getCoins().map { it.toDomain() }
            emit(Resource.Success(coins))
        } catch (e: HttpException){
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred" ) )
        } catch (e: IOException){
            emit(Resource.Error("Internet connection may be unavailable, server not reachable"))
        } catch (e: Exception){
            emit(Resource.Error("UNESPECTED ERROR: " + e.message.toString()))
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
            emit(Resource.Error("UNESPECTED ERROR: " + e.message.toString()))
        }
    }


}