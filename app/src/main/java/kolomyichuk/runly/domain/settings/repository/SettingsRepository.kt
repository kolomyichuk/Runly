package kolomyichuk.runly.domain.settings.repository

import kolomyichuk.runly.domain.settings.model.DistanceUnit
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getDistanceUnit(): Flow<DistanceUnit>
}