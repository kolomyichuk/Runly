package kolomyichuk.runly.data.repository

import kolomyichuk.runly.data.local.datastore.SettingsPreferencesDataStore
import kolomyichuk.runly.data.local.datastore.mappers.toDistanceUnitData
import kolomyichuk.runly.data.local.datastore.mappers.toDomain
import kolomyichuk.runly.data.local.datastore.model.AppTheme
import kolomyichuk.runly.data.local.datastore.model.DistanceUnitData
import kolomyichuk.runly.domain.settings.model.DistanceUnit
import kolomyichuk.runly.domain.settings.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val preferencesDataStore: SettingsPreferencesDataStore
) : SettingsRepository {
    val themeState: Flow<AppTheme> = preferencesDataStore.themeState

    val distanceUnitDataState: Flow<DistanceUnitData> = preferencesDataStore.distanceUnitDataState

    suspend fun saveTheme(theme: AppTheme) {
        preferencesDataStore.saveTheme(theme)
    }

    suspend fun saveDistanceUnit(distanceUnit: DistanceUnit) {
        preferencesDataStore.saveDistanceUnit(distanceUnit.toDistanceUnitData())
    }

    override fun getDistanceUnit(): Flow<DistanceUnit> {
        return preferencesDataStore.distanceUnitDataState.map { it.toDomain() }
    }
}



