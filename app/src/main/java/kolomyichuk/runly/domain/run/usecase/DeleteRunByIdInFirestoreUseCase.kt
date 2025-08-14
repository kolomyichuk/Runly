package kolomyichuk.runly.domain.run.usecase

import kolomyichuk.runly.domain.run.repository.RunRepository
import javax.inject.Inject

class DeleteRunByIdInFirestoreUseCase @Inject constructor(
    private val runRepository: RunRepository
) {
    suspend operator fun invoke(runId: String) {
        runRepository.deleteRunByIdInFirestore(runId)
    }
}