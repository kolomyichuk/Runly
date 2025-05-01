package kolomyichuk.runly.data.repository

import kolomyichuk.runly.data.local.room.dao.RunDao
import kolomyichuk.runly.data.local.room.entity.Run
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RunRepository (
    private val runDao: RunDao
) {
    fun getAllRuns(): Flow<List<Run>> {
        return runDao.getAllRuns()
    }

    suspend fun insertRun(run: Run) {
        runDao.insertRun(run)
    }

    suspend fun deleteRun(run: Run) {
        runDao.deleteRun(run)
    }
}

