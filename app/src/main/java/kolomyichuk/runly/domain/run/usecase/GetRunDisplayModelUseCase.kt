package kolomyichuk.runly.domain.run.usecase

import kolomyichuk.runly.domain.run.avgSpeed
import kolomyichuk.runly.domain.run.model.RunDisplayModel
import kolomyichuk.runly.domain.run.repository.RunRepository
import kolomyichuk.runly.domain.run.toDistanceUnit
import kolomyichuk.runly.domain.settings.repository.SettingsRepository
import kolomyichuk.runly.utils.FormatterUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.util.Locale
import javax.inject.Inject

class GetRunDisplayModelUseCase @Inject constructor(
    private val runRepository: RunRepository,
    private val settingsRepository: SettingsRepository,
) {
    operator fun invoke(): Flow<RunDisplayModel> {
        return combine(
            runRepository.runState,
            settingsRepository.getDistanceUnit()
        ) { runState, unit ->
            val distance = runState.distanceInMeters.toDistanceUnit(unit)
            val avgSpeed = distance.avgSpeed(runState.timeInMillis)

            RunDisplayModel(
                distance = String.format(Locale.US, "%.2f", distance),
                duration = FormatterUtils.formatTime(runState.timeInMillis),
                routePoints = runState.pathPoints,
                avgSpeed = avgSpeed,
                unit = unit,
                isActiveRun = runState.isActiveRun,
                isPause = runState.isPause,
                isTracking = runState.isTracking
            )
        }
    }
}