package kolomyichuk.runly.data.repository

import com.google.android.gms.maps.model.LatLng
import kolomyichuk.runly.data.local.datastore.SettingsPreferencesDataStore
import kolomyichuk.runly.data.local.room.dao.RunDao
import kolomyichuk.runly.data.local.room.entity.Run
import kolomyichuk.runly.data.model.RunDisplayModel
import kolomyichuk.runly.data.model.RunState
import kolomyichuk.runly.data.utils.calculateAvgSpeed
import kolomyichuk.runly.data.utils.convertDistance
import kolomyichuk.runly.utils.FormatterUtils
import kolomyichuk.runly.utils.FormatterUtils.toFormattedDateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import java.util.Locale

class RunRepository(
    private val runDao: RunDao,
    private val settingsDataStore: SettingsPreferencesDataStore
) {
    private val _runState = MutableStateFlow(RunState())
    val runState: StateFlow<RunState> = _runState.asStateFlow()

    fun updateRunState(update: RunState.() -> RunState) {
        _runState.update { it.update() }
    }

    val runDisplayState: Flow<RunDisplayModel> = combine(
        runState,
        settingsDataStore.distanceUnitState
    ) { run, unit ->
        val distance = convertDistance(run.distanceInMeters, unit)
        val avgSpeed = calculateAvgSpeed(distance, run.timeInMillis)

        RunDisplayModel(
            distance = String.format(Locale.US, "%.2f", distance),
            duration = FormatterUtils.formatTime(run.timeInMillis),
            routePoints = run.pathPoints,
            avgSpeed = avgSpeed,
            unit = unit,
            isActiveRun = run.isActiveRun,
            isPause = run.isPause,
            isTracking = run.isTracking
        )
    }

    fun getAllRuns(): Flow<List<RunDisplayModel>> {
        return combine(
            runDao.getAllRuns(),
            settingsDataStore.distanceUnitState
        ) { runs, unit ->
            runs.map { run ->
                val distance = convertDistance(run.distanceInMeters, unit)
                val avgSpeed = calculateAvgSpeed(distance, run.durationInMillis)

                RunDisplayModel(
                    id = run.id,
                    distance = String.format(Locale.US, "%.2f", distance),
                    duration = FormatterUtils.formatTime(run.durationInMillis),
                    routePoints = run.routePoints.map { path ->
                        path.map {
                            LatLng(it.latitude, it.longitude)
                        }
                    },
                    avgSpeed = avgSpeed,
                    dateTime = run.timestamp.toFormattedDateTime(),
                    unit = unit
                )
            }
        }
    }

    suspend fun insertRun(run: Run) {
        runDao.insertRun(run)
    }

    suspend fun deleteRun(run: Run) {
        runDao.deleteRun(run)
    }

    fun getRunById(runId: Int): Flow<RunDisplayModel> {
        return combine(
            runDao.getRunById(runId),
            settingsDataStore.distanceUnitState
        ) { run, unit ->
            val distance = convertDistance(run.distanceInMeters, unit)
            val avgSpeed = calculateAvgSpeed(distance, run.durationInMillis)

            RunDisplayModel(
                id = run.id,
                dateTime = run.timestamp.toFormattedDateTime(),
                distance = String.format(Locale.US, "%.2f", distance),
                avgSpeed = avgSpeed,
                routePoints = run.routePoints.map { path ->
                    path.map {
                        LatLng(it.latitude, it.longitude)
                    }
                },
                unit = unit,
                duration = FormatterUtils.formatTime(run.durationInMillis)
            )
        }
    }
}

