package com.example.domain.repository

import com.example.domain.model.Science

interface ScienceRepository {
    suspend fun getScienceList():List<Science>
}