package com.cursorcoin.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SettingsDataStore @Inject constructor(
    private val context: Context
) {
    private object PreferencesKeys {
        val USE_LOCAL_DATA = booleanPreferencesKey("use_local_data")
    }

    val useLocalData: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.USE_LOCAL_DATA] ?: false
        }

    suspend fun setUseLocalData(useLocal: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USE_LOCAL_DATA] = useLocal
        }
    }
} 