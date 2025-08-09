package kolomyichuk.runly.domain.run.usecase

import kolomyichuk.runly.domain.run.model.RoutePoint
import kolomyichuk.runly.domain.run.model.RunDisplayModel
import kolomyichuk.runly.domain.run.repository.RunRepository
import kolomyichuk.runly.domain.settings.repository.SettingsRepository
import kolomyichuk.runly.utils.FormatterUtils
import kolomyichuk.runly.utils.FormatterUtils.toFormattedDateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.util.Locale
import javax.inject.Inject

class GetRunByIdFromFirestoreUseCase @Inject constructor(
    private val runRepository: RunRepository,
    private val calculateDistanceUseCase: CalculateDistanceUseCase,
    private val calculateAvgSpeedUseCase: CalculateAvgSpeedUseCase,
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(runId: String): Flow<RunDisplayModel> {
        return combine(
            runRepository.getRunByIdFromFirestore(runId),
            settingsRepository.getDistanceUnit()
        ) { run, unit ->
            val distance = calculateDistanceUseCase(run.distanceInMeters, unit)
            val avgSpeed = calculateAvgSpeedUseCase(distance, run.durationInMillis)

            RunDisplayModel(
                id = run.id,
                distance = String.format(Locale.US, "%.2f", distance),
                duration = FormatterUtils.formatTime(run.durationInMillis),
                routePoints = run.routePoints.map { path ->
                    path.map {
                        RoutePoint(it.latitude, it.longitude)
                    }
                },
                avgSpeed = avgSpeed,
                dateTime = run.timestamp.toFormattedDateTime(),
                unit = unit
            )
        }
    }
}