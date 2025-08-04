package kolomyichuk.runly.data.repository

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kolomyichuk.runly.data.local.datastore.SettingsPreferencesDataStore
import kolomyichuk.runly.data.local.room.dao.RunDao
import kolomyichuk.runly.data.local.room.entity.RunEntity
import kolomyichuk.runly.data.local.room.mappers.toRunEntity
import kolomyichuk.runly.data.model.DistanceUnit
import kolomyichuk.runly.data.model.Run
import kolomyichuk.runly.data.model.RunDisplayModel
import kolomyichuk.runly.data.model.RunState
import kolomyichuk.runly.data.remote.firestore.mappers.toRun
import kolomyichuk.runly.data.remote.firestore.mappers.toRunFirestoreModel
import kolomyichuk.runly.data.remote.firestore.model.RunFirestoreModel
import kolomyichuk.runly.utils.FormatterUtils
import kolomyichuk.runly.utils.FormatterUtils.toFormattedDateTime
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.Locale

private const val RUNS_COLLECTION = "runs"
private const val USER_ID_FIELD = "userId"

class RunRepository(
    private val runDao: RunDao,
    private val settingsDataStore: SettingsPreferencesDataStore,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private val _runState = MutableStateFlow(RunState())
    val runState: StateFlow<RunState> = _runState.asStateFlow()

    fun updateRunState(update: RunState.() -> RunState) {
        _runState.update { it.update() }
    }

    private val userId: String
        get() = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")

    suspend fun insertRunInFirestore(run: Run) {
        try {
            val newDocRef = firestore.collection(RUNS_COLLECTION).document()
            val runWithId = run.copy(id = newDocRef.id)
            val firestoreModel = runWithId.toRunFirestoreModel(userId)
            newDocRef.set(firestoreModel).await()
        } catch (e: Exception) {
            Timber.e("Error inserting run: ${e.message}")
        }
    }

    suspend fun deleteRunByIdInFirestore(runId: String) {
        try {
            firestore.collection(RUNS_COLLECTION).document(runId).delete().await()
        } catch (e: Exception) {
            Timber.e("Error deleting error with id: $runId - ${e.message}")
        }
    }

    fun getAllRunsFromFirestore(): Flow<List<RunDisplayModel>> {
        val currentUser = auth.currentUser
        if (currentUser == null) return flowOf(emptyList())

        return callbackFlow {
            val subscription = firestore.collection(RUNS_COLLECTION)
                .whereEqualTo(USER_ID_FIELD, currentUser.uid)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }

                    val runs = snapshot?.documents
                        ?.mapNotNull {
                            it.toObject(RunFirestoreModel::class.java)?.toRun()
                        } ?: emptyList()
                    try {
                        trySend(runs)
                    } catch (e: Exception) {
                        Timber.e("Sending to flow failed: ${e.message}")
                    }
                }
            awaitClose { subscription.remove() }
        }.combine(settingsDataStore.distanceUnitState) { runs, unit ->
            runs.map { it.toRunDisplayModel(unit) }
        }
    }

    fun getRunByIdFromFirestore(runId: String): Flow<RunDisplayModel> {
        return callbackFlow {
            val docRef = firestore.collection(RUNS_COLLECTION).document(runId)
            val subscription = docRef.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Timber.e("Snapshot error: ${error.message}")
                    close(error)
                    return@addSnapshotListener
                }

                try {
                    val run =
                        snapshot?.toObject(RunFirestoreModel::class.java)
                            ?.toRun()
                    if (run != null) trySend(run)
                } catch (e: Exception) {
                    Timber.e("Error: ${e.message}")
                }
            }

            awaitClose { subscription.remove() }
        }.combine(settingsDataStore.distanceUnitState) { run, unit ->
            run.toRunDisplayModel(unit)
        }
    }

    suspend fun insertRun(run: Run) {
        runDao.insertRun(run.toRunEntity())
    }

    suspend fun deleteRunById(runId: Int) {
        runDao.deleteRunById(runId)
    }

    val runDisplayState: Flow<RunDisplayModel> = combine(
        runState,
        settingsDataStore.distanceUnitState
    ) { run, unit -> mapRunStateToDisplayModel(run, unit) }

    fun getAllRuns(): Flow<List<RunDisplayModel>> {
        return combine(
            runDao.getAllRuns(),
            settingsDataStore.distanceUnitState
        ) { runs, unit ->
            runs.map { it.toRunDisplayModel(unit) }
        }
    }

    fun getRunById(runId: Int): Flow<RunDisplayModel> {
        return combine(
            runDao.getRunById(runId),
            settingsDataStore.distanceUnitState
        ) { run, unit -> run.toRunDisplayModel(unit) }
    }

    private fun Run.toRunDisplayModel(unit: DistanceUnit): RunDisplayModel {
        val distance = convertDistance(this.distanceInMeters, unit)
        val avgSpeed = calculateAvgSpeed(distance, this.durationInMillis)

        return RunDisplayModel(
            id = this.id,
            distance = String.format(Locale.US, "%.2f", distance),
            duration = FormatterUtils.formatTime(this.durationInMillis),
            routePoints = this.routePoints.map { path ->
                path.map {
                    LatLng(it.latitude, it.longitude)
                }
            },
            avgSpeed = avgSpeed,
            dateTime = this.timestamp.toFormattedDateTime(),
            unit = unit
        )
    }

    private fun RunEntity.toRunDisplayModel(unit: DistanceUnit): RunDisplayModel {
        val distance = convertDistance(distanceInMeters, unit)
        val avgSpeed = calculateAvgSpeed(distance, durationInMillis)

        return RunDisplayModel(
            id = id.toString(),
            distance = String.format(Locale.US, "%.2f", distance),
            duration = FormatterUtils.formatTime(durationInMillis),
            routePoints = routePoints.map { path ->
                path.map {
                    LatLng(it.latitude, it.longitude)
                }
            },
            avgSpeed = avgSpeed,
            dateTime = timestamp.toFormattedDateTime(),
            unit = unit
        )
    }

    private fun mapRunStateToDisplayModel(run: RunState, unit: DistanceUnit): RunDisplayModel {
        val distance = convertDistance(run.distanceInMeters, unit)
        val avgSpeed = calculateAvgSpeed(distance, run.timeInMillis)

        return RunDisplayModel(
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

    private fun calculateAvgSpeed(distance: Double, durationInMillis: Long): String {
        val timeInSeconds = durationInMillis / 1000
        return if (timeInSeconds > 5 && distance > 0.01) {
            val speed = distance / (timeInSeconds / 3600.0)
            if (speed.isFinite()) String.format(Locale.US, "%.2f", speed) else "0.00"
        } else "0.00"
    }

    private fun convertDistance(distanceInMeters: Double, unit: DistanceUnit): Double {
        return distanceInMeters / unit.metersPerUnit
    }
}

