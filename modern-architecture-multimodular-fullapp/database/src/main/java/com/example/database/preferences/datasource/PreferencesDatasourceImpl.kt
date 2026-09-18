package com.example.database.preferences.datasource

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlin.also


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferencesDatasourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : PreferencesDatasource {
    companion object{
        val SELECTED_CITY = stringPreferencesKey("selected_city")
    }

    override val selectedCity: Flow<String?> =
        context.dataStore.data.map { preferences -> preferences[SELECTED_CITY] }

    override suspend fun toggleSelectCity(city: String?) {
        context.dataStore.updateData {
            it.toMutablePreferences().also { preferences ->
                if(city == null) preferences.remove(SELECTED_CITY)
                else preferences[SELECTED_CITY] = city
            }
        }
    }


}
