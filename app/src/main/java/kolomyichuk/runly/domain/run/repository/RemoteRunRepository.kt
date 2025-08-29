package kolomyichuk.runly.domain.run.repository

import kolomyichuk.runly.domain.run.model.Run
import kolomyichuk.runly.domain.run.model.RunChart
import kolomyichuk.runly.domain.run.model.RunState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.util.Date

interface RemoteRunRepository {
    val runState: StateFlow<RunState>
    fun updateRunState(update: RunState.() -> RunState)

    suspend fun insertRunInFirestore(run: Run)
    suspend fun deleteRunByIdInFirestore(runId: String)
    fun getAllRunsFromFirestore(): Flow<List<Run>>
    fun getRunByIdFromFirestore(runId: String): Flow<Run>
    suspend fun getThisWeekDistanceByDay(start: Date, end: Date): List<RunChart>
    suspend fun getTotalDistance(): Double
}
