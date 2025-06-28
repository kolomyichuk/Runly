package kolomyichuk.runly.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kolomyichuk.runly.data.model.AppTheme
import kolomyichuk.runly.data.model.DistanceUnit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsPreferencesDataStore(
    private val dataStore: DataStore<Preferences>
) {
    val themeState: Flow<AppTheme> = dataStore.data
        .map { preferences ->
            val themeName = preferences[THEME_KEY] ?: AppTheme.SYSTEM.name
            AppTheme.entries.find { it.name == themeName } ?: AppTheme.SYSTEM
        }

    val distanceUnitState: Flow<DistanceUnit> = dataStore.data
        .map { preferences ->
            val ordinal = preferences[DISTANCE_UNIT_KEY] ?: DistanceUnit.KILOMETERS.ordinal
            DistanceUnit.entries.getOrNull(ordinal) ?: DistanceUnit.KILOMETERS
        }


    suspend fun saveTheme(theme: AppTheme) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = theme.name
        }
    }

    suspend fun saveDistanceUnit(distanceUnit: DistanceUnit) {
        dataStore.edit { preferences ->
            preferences[DISTANCE_UNIT_KEY] = distanceUnit.ordinal
        }
    }

    companion object {
        private val THEME_KEY = stringPreferencesKey("app_theme")
        private val DISTANCE_UNIT_KEY = intPreferencesKey("distance_unit")
    }
}


