package kolomyichuk.runly.domain.settings.repository

import kolomyichuk.runly.domain.settings.model.AppTheme
import kolomyichuk.runly.domain.settings.model.DistanceUnit
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val themeState: Flow<AppTheme>
    val distanceUnitDataState: Flow<DistanceUnit>
    fun getDistanceUnit(): Flow<DistanceUnit>
    suspend fun saveDistanceUnit(distanceUnit: DistanceUnit)
    suspend fun saveTheme(theme: AppTheme)
}