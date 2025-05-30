package kolomyichuk.runly.data.repository

import kolomyichuk.runly.data.local.room.dao.RunDao
import kolomyichuk.runly.data.local.room.entity.Run
import kolomyichuk.runly.data.model.RunState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RunRepository(
    private val runDao: RunDao
) {
    private val _runState = MutableStateFlow(RunState())
    val runState: StateFlow<RunState> = _runState.asStateFlow()

    fun updateRunState(update: RunState.() -> RunState) {
        _runState.update { it.update() }
    }

    fun getAllRuns(): Flow<List<Run>> {
        return runDao.getAllRuns()
    }

    suspend fun insertRun(run: Run) {
        runDao.insertRun(run)
    }

    suspend fun deleteRun(run: Run) {
        runDao.deleteRun(run)
    }

    suspend fun getRunById(runId: Int): Run {
        return runDao.getRunById(runId)
    }
}

