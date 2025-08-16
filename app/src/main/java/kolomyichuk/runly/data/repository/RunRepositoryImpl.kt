package kolomyichuk.runly.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kolomyichuk.runly.data.local.room.dao.RunDao
import kolomyichuk.runly.data.local.room.mappers.toRun
import kolomyichuk.runly.data.local.room.mappers.toRunEntity
import kolomyichuk.runly.data.remote.firestore.mappers.toRun
import kolomyichuk.runly.data.remote.firestore.mappers.toRunFirestoreModel
import kolomyichuk.runly.data.remote.firestore.model.RunFirestoreModel
import kolomyichuk.runly.domain.run.model.Run
import kolomyichuk.runly.domain.run.model.RunState
import kolomyichuk.runly.domain.run.repository.RunRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import timber.log.Timber

private const val RUNS_COLLECTION = "runs"
private const val USER_ID_FIELD = "userId"
private const val TIMESTAMP_FIELD = "timestamp"

class RunRepositoryImpl(
    private val runDao: RunDao,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : RunRepository {
    private val _runState = MutableStateFlow(RunState())
    override val runState: StateFlow<RunState> = _runState.asStateFlow()

    override fun updateRunState(update: RunState.() -> RunState) {
        _runState.update { it.update() }
    }

    private val userId: String
        get() = auth.currentUser?.uid ?: error("User not logged in")

    override suspend fun insertRunInFirestore(run: Run) {
        try {
            val newDocRef = firestore.collection(RUNS_COLLECTION).document()
            val runWithId = run.copy(id = newDocRef.id)
            val firestoreModel = runWithId.toRunFirestoreModel(userId)
            newDocRef.set(firestoreModel).await()
        } catch (e: FirebaseFirestoreException) {
            Timber.e(e, "Error inserting run into Firestore")
        }
    }

    override suspend fun deleteRunByIdInFirestore(runId: String) {
        try {
            firestore.collection(RUNS_COLLECTION).document(runId).delete().await()
        } catch (e: FirebaseFirestoreException) {
            Timber.e(e, "Error deleting run with id: $runId")
        }
    }

    override fun getAllRunsFromFirestore(): Flow<List<Run>> {
        val currentUser = auth.currentUser ?: return flowOf(emptyList())

        return callbackFlow {
            val subscription = firestore.collection(RUNS_COLLECTION)
                .whereEqualTo(USER_ID_FIELD, currentUser.uid)
                .orderBy(TIMESTAMP_FIELD, Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }

                    val runs = snapshot?.documents
                        ?.mapNotNull {
                            it.toObject(RunFirestoreModel::class.java)?.toRun()
                        } ?: emptyList()

                    trySend(runs)
                }
            awaitClose { subscription.remove() }
        }
    }

    override fun getRunByIdFromFirestore(runId: String): Flow<Run> {
        return callbackFlow {
            val docRef = firestore.collection(RUNS_COLLECTION).document(runId)
            val subscription = docRef.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Timber.e("Snapshot error: ${error.message}")
                    close(error)
                    return@addSnapshotListener
                }

                snapshot?.toObject(RunFirestoreModel::class.java)?.toRun()?.let {
                    trySend(it).isSuccess
                }
            }

            awaitClose { subscription.remove() }
        }
    }

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
