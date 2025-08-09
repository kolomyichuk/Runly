package kolomyichuk.runly.domain.run.usecase

import kolomyichuk.runly.domain.run.model.RunState
import kolomyichuk.runly.domain.run.repository.RunRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetRunStateUseCase @Inject constructor(
    private val runRepository: RunRepository
) {
    operator fun invoke(): StateFlow<RunState> {
        return runRepository.runState
    }
}