package kolomyichuk.runly.domain.run.usecase

import kolomyichuk.runly.domain.run.model.Run
import kolomyichuk.runly.domain.run.repository.RunRepository

class InsertRunInFirestoreUseCase(
    private val runRepository: RunRepository
) {
    suspend operator fun invoke(run: Run) {
        runRepository.insertRunInFirestore(run)
    }
}
