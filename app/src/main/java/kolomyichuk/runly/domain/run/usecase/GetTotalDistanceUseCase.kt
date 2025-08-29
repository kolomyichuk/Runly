package kolomyichuk.runly.domain.run.usecase

import kolomyichuk.runly.domain.run.RunCalculations
import kolomyichuk.runly.domain.run.repository.RemoteRunRepository
import kolomyichuk.runly.domain.settings.repository.SettingsRepository
import kotlinx.coroutines.flow.first
import java.util.Locale

class GetTotalDistanceUseCase(
    private val remoteRunRepository: RemoteRunRepository,
    private val settingsRepository: SettingsRepository
) {
    suspend fun invoke(): Float {
        val unit = settingsRepository.getDistanceUnit().first()
        val totalMeters = remoteRunRepository.getTotalDistance()
        val distance = RunCalculations.convertDistance(totalMeters, unit)
        return String.format(Locale.US,"%.2f", distance).toFloat()
    }
}
