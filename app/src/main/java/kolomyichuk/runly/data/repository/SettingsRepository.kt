package kolomyichuk.runly.data.repository

import kolomyichuk.runly.data.local.datastore.SettingsPreferencesDataStore
import kolomyichuk.runly.data.model.AppTheme
import kolomyichuk.runly.data.model.DistanceUnit
import kotlinx.coroutines.flow.Flow

class SettingsRepository(
    private val preferencesDataStore: SettingsPreferencesDataStore
) {
    val themeState: Flow<AppTheme> = preferencesDataStore.themeState

    val distanceUnitState: Flow<DistanceUnit> = preferencesDataStore.distanceUnitState

    suspend fun saveTheme(theme: AppTheme) {
        preferencesDataStore.saveTheme(theme)
    }

    suspend fun saveDistanceUnit(distanceUnit: DistanceUnit) {
        preferencesDataStore.saveDistanceUnit(distanceUnit)
    }
}



