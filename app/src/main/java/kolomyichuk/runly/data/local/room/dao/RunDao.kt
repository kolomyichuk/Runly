package kolomyichuk.runly.data.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kolomyichuk.runly.data.local.room.entity.Run
import kotlinx.coroutines.flow.Flow

@Dao
interface RunDao {
    @Insert
    suspend fun insertRun(run: Run)

    @Query("SELECT * FROM runs ORDER BY timestamp DESC")
    fun getAllRuns(): Flow<List<Run>>

    @Query("SELECT * FROM runs WHERE id = :runId")
    fun getRunById(runId: Int): Flow<Run>

    @Delete
    suspend fun deleteRun(run: Run)
}