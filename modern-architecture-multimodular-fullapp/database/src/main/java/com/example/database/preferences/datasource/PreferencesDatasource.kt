package com.example.database.preferences.datasource

import kotlinx.coroutines.flow.Flow

interface PreferencesDatasource {
    val selectedCity: Flow<String?>
    suspend fun toggleSelectCity(city: String?)
}