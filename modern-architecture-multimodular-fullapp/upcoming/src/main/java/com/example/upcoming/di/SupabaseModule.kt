package com.example.upcoming.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {
    @Provides
    @Singleton
    fun provideSupabase(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = "https://tfjxgdaextargramukeq.supabase.co",
            supabaseKey = "sb_publishable_kkDvXeZbEPa73K0gfGg7Wg__N3j_ofa"
        ) {
            install(Postgrest)
        }
    }
}
