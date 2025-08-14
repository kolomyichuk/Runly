package kolomyichuk.runly.data.repository

import kolomyichuk.runly.data.local.datastore.SettingsPreferencesDataStore
import kolomyichuk.runly.domain.settings.model.AppTheme
import kolomyichuk.runly.domain.settings.model.DistanceUnit
import kolomyichuk.runly.domain.settings.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class SettingsRepositoryImpl(
    private val preferencesDataStore: SettingsPreferencesDataStore
) : SettingsRepository {
    override val themeState: Flow<AppTheme> = preferencesDataStore.themeState

    override val distanceUnitDataState: Flow<DistanceUnit> =
        preferencesDataStore.distanceUnitDataState

    override suspend fun saveTheme(theme: AppTheme) {
        preferencesDataStore.saveTheme(theme)
    }

    override suspend fun saveDistanceUnit(distanceUnit: DistanceUnit) {
        preferencesDataStore.saveDistanceUnit(distanceUnit)
    }

    override fun getDistanceUnit(): Flow<DistanceUnit> {
        return preferencesDataStore.distanceUnitDataState
    }
}



