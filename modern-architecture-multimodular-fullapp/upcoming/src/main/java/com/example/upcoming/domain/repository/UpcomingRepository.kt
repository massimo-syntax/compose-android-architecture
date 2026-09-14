package com.example.upcoming.domain.repository

import com.example.upcoming.domain.model.Upcoming

interface UpcomingRepository {
    suspend fun getUpcomingItems(): List<Upcoming>
}
