package com.example.data.repository


import androidx.room.withTransaction
import com.example.data.mapper.toDomain
import com.example.data.mapper.toEntity
import com.example.data_local.ScienceDatabase
import com.example.data_local.dao.ScienceDao
import com.example.data_remote.apiResult.ApiResult
import com.example.data_remote.dataSource.ScienceRemoteDataSource
import com.example.domain.model.EventualNetworkFeedback
import com.example.domain.model.Science
import com.example.domain.repository.ScienceRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ScienceRepositoryImpl @Inject constructor(
    private val dataSource: ScienceRemoteDataSource,
    private val database: ScienceDatabase,
    private val dao: ScienceDao
) : ScienceRepository {

    override fun observeScienceDb(): Flow<List<Science>> {
        return dao.observeScienceList()
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }

    override suspend fun requestAndSaveScienceList(): EventualNetworkFeedback {
        return when(val apiResult = dataSource.getScienceList()){
            is ApiResult.Success -> {
                val data  = apiResult.data.map { it.toEntity() }
                database.withTransaction {
                    dao.clearAll()
                    dao.insertAll(data)
                }
                EventualNetworkFeedback(true, "data loaded")
            }
            is ApiResult.Failure -> {
                EventualNetworkFeedback(
                    success = false,
                    message = """
                        Error: ${apiResult.kind.name}
                        Message: ${apiResult.message ?: "no message"}
                        Cause: ${apiResult.cause?.message ?: "unknown cause"}
                        Code: ${apiResult.code ?: "unknown"} 
                    """.trimIndent()
                )
            }
        }
    }

}