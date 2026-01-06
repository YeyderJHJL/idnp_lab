package com.example.composenavdemo.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Extensión para crear el DataStore
 * Se crea una sola instancia por aplicación
 */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "airsense_preferences"
)

/**
 * Manager para gestionar preferencias con DataStore
 *
 * PRÁCTICA 6: Persistencia con DataStore
 * - Guarda tema (claro/oscuro)
 * - Guarda preferencias de usuario
 * - Datos persisten entre sesiones
 */
class PreferencesManager(private val context: Context) {

    /**
     * Keys para las preferencias
     */
    private object PreferencesKeys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val LOCATION_PERMISSION = booleanPreferencesKey("location_permission")
        val LANGUAGE = stringPreferencesKey("language")
        val USER_NAME = stringPreferencesKey("user_name")
    }

    /**
     * Enum para los modos de tema
     */
    enum class ThemeMode {
        LIGHT,      // Tema claro
        DARK,       // Tema oscuro
        SYSTEM;     // Seguir el sistema

        companion object {
            fun fromString(value: String): ThemeMode {
                return try {
                    valueOf(value)
                } catch (e: IllegalArgumentException) {
                    SYSTEM // Default
                }
            }
        }
    }

    // --- FLUJOS DE DATOS (Flows) ---

    val themeMode: Flow<ThemeMode> = context.dataStore.data.map { preferences ->
        ThemeMode.fromString(preferences[PreferencesKeys.THEME_MODE] ?: ThemeMode.SYSTEM.name)
    }

    val dynamicColor: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.DYNAMIC_COLOR] ?: true // Default a true
    }

    val notificationsEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] ?: true
    }

    val userName: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USER_NAME] ?: ""
    }


    // --- FUNCIONES DE GUARDADO Y BORRADO ---

    suspend fun saveThemeMode(themeMode: ThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_MODE] = themeMode.name
        }
    }

    suspend fun saveDynamicColor(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DYNAMIC_COLOR] = enabled
        }
    }

    suspend fun saveNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] = enabled
        }
    }

    suspend fun saveUserName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_NAME] = name
        }
    }

    suspend fun clearAllPreferences() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}