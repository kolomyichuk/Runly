package kolomyichuk.runly.domain.run.usecase

import kolomyichuk.runly.domain.run.repository.RunRemoteRepository

class DeleteRunByIdInFirestoreUseCase(
    private val runRemoteRepository: RunRemoteRepository
) {
    suspend operator fun invoke(runId: String) {
        runRemoteRepository.deleteRunByIdInFirestore(runId)
    }
}
