package kolomyichuk.runly.domain.run.usecase

import kolomyichuk.runly.domain.run.model.RunDisplayModel
import kolomyichuk.runly.domain.run.repository.RunRepository
import kolomyichuk.runly.domain.run.toRunDisplayModel
import kolomyichuk.runly.domain.settings.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetAllRunsFromFirestoreUseCase @Inject constructor(
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