package kolomyichuk.runly.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kolomyichuk.runly.di.ThemePreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeRepository @Inject constructor(
    @ThemePreferences private val dataStore: DataStore<Preferences>
) {
    val themeFlow: Flow<AppTheme> = dataStore.data
        .map { preferences ->
            val themeName = preferences[THEME_KEY] ?: AppTheme.SYSTEM.name
            AppTheme.entries.find { it.name == themeName } ?: AppTheme.SYSTEM
        }

    suspend fun saveTheme(theme: AppTheme) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = theme.name
        }
    }

    companion object {
        private val THEME_KEY = stringPreferencesKey("app_theme")
    }
}

enum class AppTheme {
    LIGHT, DARK, SYSTEM
}




