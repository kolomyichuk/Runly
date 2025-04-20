package kolomyichuk.runly.data.repository

import kolomyichuk.runly.data.local.room.dao.RunDao
import kolomyichuk.runly.data.local.room.entity.Run
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RunRepository @Inject constructor(
    private val runDao: RunDao
) {
    suspend fun getAllRun(): Flow<List<Run>> {
        return runDao.getAllRun()
    }

    suspend fun insertRun(run: Run) {
        runDao.insertRun(run)
    }

    suspend fun deleteRun(run: Run) {
        runDao.deleteRun(run)
    }
}

