package com.example.multimodularmodernarchitecturefullapp.di

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
    fun provideSupabase(): SupabaseClient{
        return createSupabaseClient(
            supabaseUrl = "https://tfjxgdaextargramukeq.supabase.co",
            supabaseKey = "sb_publishable_kkDvXeZbEPa73K0gfGg7Wg__N3j_ofa"
        ) {
            install(Postgrest)
        }
    }
}

// instructions
// https://supabase.com/dashboard
// + new project
// project ->
// table editor from menu
// new table and so on
// disable RLS for easy testing, or it returns an empty array

// add dependencies
//implementation(platform("io.github.jan-tennert.supabase:bom:3.8.0"))
//implementation("io.github.jan-tennert.supabase:supabase-kt")
//implementation("io.github.jan-tennert.supabase:postgrest-kt")
//implementation("io.ktor:ktor-client-android:3.5.2")
// kotlinx.serialization plugin is necessary
// wherever supabase client is needed same as code above
// create data class abc(a,b,@SerialName"c_c")..
// val myData = supabase.from("whatevertablename").select().decodeList<Abc>()
// done.