package kolomyichuk.runly.domain.run.usecase

import kolomyichuk.runly.domain.run.model.Run
import kolomyichuk.runly.domain.run.repository.RemoteRunRepository

class InsertRunInFirestoreUseCase(
    private val remoteRunRepository: RemoteRunRepository
) {
    suspend operator fun invoke(run: Run) {
        remoteRunRepository.insertRunInFirestore(run)
    }
}
