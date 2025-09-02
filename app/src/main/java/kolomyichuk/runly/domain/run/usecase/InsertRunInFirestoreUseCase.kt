package kolomyichuk.runly.domain.run.usecase

import kolomyichuk.runly.domain.run.model.Run
import kolomyichuk.runly.domain.run.repository.RunRemoteRepository

class InsertRunInFirestoreUseCase(
    private val runRemoteRepository: RunRemoteRepository
) {
    suspend operator fun invoke(run: Run) {
        runRemoteRepository.insertRunInFirestore(run)
    }
}
