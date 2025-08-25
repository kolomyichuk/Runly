package kolomyichuk.runly.domain.run.usecase

import kolomyichuk.runly.domain.run.ext.toRunDisplayModel
import kolomyichuk.runly.domain.run.model.RunDisplayModel
import kolomyichuk.runly.domain.run.repository.RunRepository
import kolomyichuk.runly.domain.settings.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetRunByIdFromFirestoreUseCase(
    private val runRepository: RunRepository,
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(runId: String): Flow<RunDisplayModel> {
        return combine(
            runRepository.getRunByIdFromFirestore(runId),
            settingsRepository.getDistanceUnit()
        ) { run, unit ->
            run.toRunDisplayModel(unit)
        }
    }
}
