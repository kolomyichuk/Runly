package kolomyichuk.runly.domain.run.usecase

import kolomyichuk.runly.domain.run.RunCalculations
import kolomyichuk.runly.domain.run.repository.RunRemoteRepository
import kolomyichuk.runly.domain.settings.repository.SettingsRepository
import kotlinx.coroutines.flow.first
import java.util.Locale

class GetTotalDistanceUseCase(
    private val runRemoteRepository: RunRemoteRepository,
    private val settingsRepository: SettingsRepository
) {
    suspend fun invoke(): Float {
        val unit = settingsRepository.getDistanceUnit().first()
        val totalMeters = runRemoteRepository.getTotalDistance()
        val distance = RunCalculations.convertDistance(totalMeters, unit)
        return String.format(Locale.US,"%.2f", distance).toFloat()
    }
}
