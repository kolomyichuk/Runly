package kolomyichuk.runly.domain.run.usecase

import kolomyichuk.runly.domain.run.ext.toRunDisplayModel
import kolomyichuk.runly.domain.run.model.RunDisplayModel
import kolomyichuk.runly.domain.run.repository.RunRepository
import kolomyichuk.runly.domain.settings.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetAllRunsFromFirestoreUseCase(
    private val runRepository: RunRepository,
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<List<RunDisplayModel>> {
        return combine(
            runRepository.getAllRunsFromFirestore(),
            settingsRepository.getDistanceUnit()
        ) { runs, unit ->
            runs.map { run ->
                run.toRunDisplayModel(unit)
            }
        }
    }
}
