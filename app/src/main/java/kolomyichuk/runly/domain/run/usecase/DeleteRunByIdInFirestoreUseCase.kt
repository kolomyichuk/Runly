package kolomyichuk.runly.domain.run.usecase

import kolomyichuk.runly.domain.run.repository.RunRepository

class DeleteRunByIdInFirestoreUseCase(
    private val runRepository: RunRepository
) {
    suspend operator fun invoke(runId: String) {
        runRepository.deleteRunByIdInFirestore(runId)
    }
}
