package kolomyichuk.runly.domain.run.usecase

import kolomyichuk.runly.domain.run.ext.toRunDisplayModel
import kolomyichuk.runly.domain.run.model.RunDisplayModel
import kolomyichuk.runly.domain.run.repository.RunRemoteRepository
import kolomyichuk.runly.domain.settings.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetRunByIdFromFirestoreUseCase(
    private val runRemoteRepository: RunRemoteRepository,
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(runId: String): Flow<RunDisplayModel> {
        return combine(
            runRemoteRepository.getRunByIdFromFirestore(runId),
            settingsRepository.getDistanceUnit()
        ) { run, unit ->
            run.toRunDisplayModel(unit)
        }
    }
}
