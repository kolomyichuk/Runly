package kolomyichuk.runly.data.repository

import kolomyichuk.runly.data.local.room.dao.RunDao
import kolomyichuk.runly.data.local.room.mappers.toRun
import kolomyichuk.runly.data.local.room.mappers.toRunEntity
import kolomyichuk.runly.domain.run.model.Run
import kolomyichuk.runly.domain.run.repository.RunLocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RunLocalRepositoryImpl(
    private val runDao: RunDao
) : RunLocalRepository {
    override suspend fun insertRun(run: Run) {
        runDao.insertRun(run.toRunEntity())
    }

    override suspend fun deleteRunById(runId: Int) {
        runDao.deleteRunById(runId)
    }

    override fun getAllRuns(): Flow<List<Run>> {
        return runDao.getAllRuns().map { list ->
            list.map { it.toRun() }
        }
    }

    override fun getRunById(runId: Int): Flow<Run> {
        return runDao.getRunById(runId).map { run ->
            run.toRun()
        }
    }
}
