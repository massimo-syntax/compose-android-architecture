package com.example.data_local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data_local.entity.ScienceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScienceDao {

    @Query("SELECT * FROM science")
    fun observeScienceList(): Flow<List<ScienceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(scienceDiscoveries: List<ScienceEntity>)

    @Query("DELETE FROM science")
    suspend fun clearAll()
}