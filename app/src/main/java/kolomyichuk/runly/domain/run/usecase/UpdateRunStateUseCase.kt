package kolomyichuk.runly.domain.run.usecase

import kolomyichuk.runly.domain.run.model.RunState
import kolomyichuk.runly.domain.run.repository.RunRepository
import javax.inject.Inject

class UpdateRunStateUseCase @Inject constructor(
    private val runRepository: RunRepository
) {
    operator fun invoke(update: RunState.() -> RunState) {
        runRepository.updateRunState(update)
    }
}