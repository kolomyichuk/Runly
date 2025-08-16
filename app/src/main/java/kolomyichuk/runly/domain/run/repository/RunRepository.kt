package kolomyichuk.runly.domain.run.repository

import kolomyichuk.runly.domain.run.model.Run
import kolomyichuk.runly.domain.run.model.RunDisplayModel
import kolomyichuk.runly.domain.run.model.RunState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface RunRepository {
    val runState: StateFlow<RunState>
    fun updateRunState(update: RunState.() -> RunState)

    suspend fun insertRunInFirestore(run: Run)
    suspend fun deleteRunByIdInFirestore(runId: String)
    fun getAllRunsFromFirestore(): Flow<List<Run>>
    fun getRunByIdFromFirestore(runId: String): Flow<Run>

    suspend fun insertRun(run: Run)
    suspend fun deleteRunById(runId: Int)
    fun getAllRuns(): Flow<List<Run>>
    fun getRunById(runId: Int): Flow<Run>
}
