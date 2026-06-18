package com.hetanshis.studyplanner.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.userDataStore by preferencesDataStore(name = "user_prefs")

class UserPreferencesRepository(
    private val context: Context
) {

    companion object {
        private val KEY_USERNAME = stringPreferencesKey("username")
        private val KEY_DARK_THEME = booleanPreferencesKey("dark_theme")
    }

    val usernameFlow: Flow<String> = context.userDataStore.data.map { prefs ->
        prefs[KEY_USERNAME] ?: "Student"
    }

    val darkThemeFlow: Flow<Boolean> = context.userDataStore.data.map { prefs ->
        prefs[KEY_DARK_THEME] ?: false
    }

    suspend fun setUsername(name: String) {
        context.userDataStore.edit { prefs ->
            prefs[KEY_USERNAME] = name
        }
    }

    suspend fun setDarkTheme(enabled: Boolean) {
        context.userDataStore.edit { prefs ->
            prefs[KEY_DARK_THEME] = enabled
        }
    }
}
