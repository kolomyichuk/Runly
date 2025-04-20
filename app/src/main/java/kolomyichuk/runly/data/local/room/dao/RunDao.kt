package kolomyichuk.runly.data.local.room.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kolomyichuk.runly.data.local.room.entity.Run
import kotlinx.coroutines.flow.Flow

interface RunDao {
    @Insert
    suspend fun insertRun(run: Run)

    @Query("SELECT * FROM runs ORDER BY timestamp DESC")
    suspend fun getAllRuns(): Flow<List<Run>>

    @Delete
    suspend fun deleteRun(run: Run)
}