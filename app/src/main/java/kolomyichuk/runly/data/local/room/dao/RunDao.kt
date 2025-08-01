package kolomyichuk.runly.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kolomyichuk.runly.data.local.room.entity.RunEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RunDao {
    @Insert
    suspend fun insertRun(runRoomModel: RunEntity)

    @Query("SELECT * FROM runs ORDER BY timestamp DESC")
    fun getAllRuns(): Flow<List<RunEntity>>

    @Query("SELECT * FROM runs WHERE id = :runId")
    fun getRunById(runId: Int): Flow<RunEntity>

    @Query("DELETE FROM runs WHERE id = :runId")
    suspend fun deleteRunById(runId: Int)
}