package com.example.data.repository

import android.os.Build
import androidx.annotation.RequiresExtension
import com.example.data.mapper.toDomain
import com.example.data_remote.dataSource.ScienceRemoteDataSource
import com.example.domain.model.Science
import com.example.domain.repository.ScienceRepository
import jakarta.inject.Inject

class ScienceRepositoryImpl @Inject constructor(
    private val dataSource: ScienceRemoteDataSource
) : ScienceRepository {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun getScienceList() : List<Science> =
        dataSource.getScienceList().map { it.toDomain() }
}