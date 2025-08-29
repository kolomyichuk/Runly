package kolomyichuk.runly.domain.run.usecase

import kolomyichuk.runly.domain.run.repository.RemoteRunRepository

class DeleteRunByIdInFirestoreUseCase(
    private val remoteRunRepository: RemoteRunRepository
) {
    suspend operator fun invoke(runId: String) {
        remoteRunRepository.deleteRunByIdInFirestore(runId)
    }
}
