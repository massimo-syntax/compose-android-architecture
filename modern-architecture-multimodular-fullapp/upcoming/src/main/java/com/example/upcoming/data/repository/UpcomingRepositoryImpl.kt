package com.example.upcoming.data.repository

import com.example.upcoming.domain.model.Upcoming
import com.example.upcoming.domain.repository.UpcomingRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import javax.inject.Inject

class UpcomingRepositoryImpl @Inject constructor(
    private val supabase: SupabaseClient
) : UpcomingRepository {
    override suspend fun getUpcomingItems(): List<Upcoming> {
        return supabase.from("upcoming")
            .select().decodeList<Upcoming>()
    }
}
