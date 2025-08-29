package kolomyichuk.runly.domain.run.repository

import kolomyichuk.runly.domain.run.model.Run
import kotlinx.coroutines.flow.Flow

interface LocalRunRepository {
    suspend fun insertRun(run: Run)
    suspend fun deleteRunById(runId: Int)
    fun getAllRuns(): Flow<List<Run>>
    fun getRunById(runId: Int): Flow<Run>
}
