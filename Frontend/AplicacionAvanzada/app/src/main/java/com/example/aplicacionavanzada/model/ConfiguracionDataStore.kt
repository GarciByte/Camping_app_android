package com.example.aplicacionavanzada.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.aplicacionavanzada.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ConfiguracionDataStore(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("configuracion_camping")

        val NOTIFICATIONS_KEY = booleanPreferencesKey("notifications")
        val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
        val TENT_RENTAL_KEY = booleanPreferencesKey("tent_rental")
        val WIFI_ACCESS_KEY = booleanPreferencesKey("wifi_access")
        val BREAKFAST_KEY = booleanPreferencesKey("breakfast")
        val LANDSCAPE_TYPE_KEY = stringPreferencesKey("landscape_type")
    }

    // Obtener las configuraciones guardadas
    val notificationsFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[NOTIFICATIONS_KEY] ?: false }

    val darkModeFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[DARK_MODE_KEY] ?: false }

    val tentRentalFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[TENT_RENTAL_KEY] ?: false }

    val wifiAccessFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[WIFI_ACCESS_KEY] ?: false }

    val breakfastFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[BREAKFAST_KEY] ?: false }

    val landscapeTypeFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[LANDSCAPE_TYPE_KEY] ?: context.getString(R.string.mountain) }

    // Guardar las configuraciones en DataStore
    suspend fun saveNotifications(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATIONS_KEY] = enabled
        }
    }

    suspend fun saveDarkMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = enabled
        }
    }

    suspend fun saveTentRental(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[TENT_RENTAL_KEY] = enabled
        }
    }

    suspend fun saveWifiAccess(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[WIFI_ACCESS_KEY] = enabled
        }
    }

    suspend fun saveBreakfast(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[BREAKFAST_KEY] = enabled
        }
    }

    suspend fun saveLandscapeType(type: String) {
        context.dataStore.edit { preferences ->
            preferences[LANDSCAPE_TYPE_KEY] = type
        }
    }


}