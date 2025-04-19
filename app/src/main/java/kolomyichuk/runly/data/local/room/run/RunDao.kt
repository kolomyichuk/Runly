package kolomyichuk.runly.data.local.room.run

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

interface RunDao {
    @Insert
    suspend fun insertRun(run: Run)

    @Query("SELECT * FROM runs ORDER BY timestamp DESC")
    suspend fun getAllRun(): Flow<List<Run>>

    @Delete
    fun deleteRun(run: Run)
}