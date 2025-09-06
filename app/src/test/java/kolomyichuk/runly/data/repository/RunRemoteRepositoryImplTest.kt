package kolomyichuk.runly.data.repository

import app.cash.turbine.test
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kolomyichuk.runly.data.remote.firestore.mappers.toRun
import kolomyichuk.runly.data.remote.firestore.model.RunFirestoreModel
import kolomyichuk.runly.domain.run.model.RunState
import kolomyichuk.runly.utils.createRun1
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.Instant

private const val TEST_RUNS_COLLECTION = "runs"
private const val TEST_USER_ID = "test_user_id"
private const val TEST_USER_ID_FIELD = "userId"
private const val TEST_TIMESTAMP_FIELD = "timestamp"
private const val TEST_RUN_ID = "testId"
private const val TEST_DISTANCE_IN_METERS_FIELD = "distanceInMeters"

class RunRemoteRepositoryImplTest {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var runRemoteRepository: RunRemoteRepositoryImpl
    private lateinit var collectionRef: CollectionReference
    private lateinit var documentRef: DocumentReference
    private lateinit var user: FirebaseUser
    private lateinit var query: Query
    private lateinit var querySnapshot: QuerySnapshot
    private lateinit var documentSnapshot: QueryDocumentSnapshot
    private lateinit var listenerRegistration: ListenerRegistration

    @Before
    fun setup() {
        auth = mockk<FirebaseAuth>(relaxed = true)
        firestore = mockk<FirebaseFirestore>(relaxed = true)
        runRemoteRepository = RunRemoteRepositoryImpl(auth = auth, firestore = firestore)
        collectionRef = mockk<CollectionReference>()
        documentRef = mockk<DocumentReference>()
        user = mockk<FirebaseUser>()
        query = mockk(relaxed = true)
        querySnapshot = mockk()
        documentSnapshot = mockk()
        listenerRegistration = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `Given initial runState, When updateRunState is called, Then runState emits new state`() =
        runTest {
            runRemoteRepository.runState.test {
                // Given
                val initial = awaitItem()
                assertEquals(RunState(), initial)

                // When
                val newState = RunState(distanceInMeters = 4000.0)
                runRemoteRepository.updateRunState { newState }

                // Then
                val emitted = awaitItem()
                assertEquals(newState, emitted)

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `Given valid user, When insertRunInFirestore called, Then run is converted and set in Firestore`() =
        runTest {
            // Given
            coEvery { auth.currentUser } returns user
            coEvery { user.uid } returns "uid123"

            coEvery { documentRef.id } returns "mockId"

            val task: Task<Void> = Tasks.forResult(null)
            every { documentRef.set(any<RunFirestoreModel>()) } returns task
            coEvery { documentRef.set(any<RunFirestoreModel>()) } returns task

            coEvery { firestore.collection(TEST_RUNS_COLLECTION) } returns collectionRef
            coEvery { collectionRef.document() } returns documentRef

            val run = createRun1()

            // When
            runRemoteRepository.insertRunInFirestore(run)

            // Then
            val slot = slot<RunFirestoreModel>()
            coVerify { documentRef.set(capture(slot)) }

            val captured = slot.captured
            assertEquals("uid123", captured.userId)
            assertEquals(500.0, captured.distanceInMeters, 0.0)
            assertEquals(5000, captured.durationInMillis)
            assertEquals("mockId", captured.id)
        }

    @Test(expected = IllegalStateException::class)
    fun `Given no user, When insertRunInFirestore called, Then IllegalStateException is thrown`() =
        runTest {
            // Given
            coEvery { auth.currentUser } returns null
            val run = createRun1()

            // When
            runRemoteRepository.insertRunInFirestore(run)
        }

    @Test
    fun `Given runId, When deleteRunByIdInFirestore called, Then delete is invoked`() = runTest {
        // Given
        coEvery {
            firestore.collection(TEST_RUNS_COLLECTION).document(TEST_RUN_ID)
        } returns documentRef
        coEvery { documentRef.delete() } returns Tasks.forResult(null)

        // When
        runRemoteRepository.deleteRunByIdInFirestore(TEST_RUN_ID)

        // Then
        coVerify { documentRef.delete() }
    }

    @Test
    fun `Given runId, When delete fails, Then FirebaseFirestoreException is caught`() = runTest {
        // Given
        val exception = mockk<FirebaseFirestoreException>(relaxed = true)
        coEvery {
            firestore.collection(TEST_RUNS_COLLECTION).document(TEST_RUN_ID)
        } returns documentRef
        coEvery { documentRef.delete() } returns Tasks.forException(exception)

        // When
        runRemoteRepository.deleteRunByIdInFirestore(TEST_RUN_ID)

        // Then
        verify { documentRef.delete() }
    }


    @Test
    fun `Given valid user, When getAllRunsFromFirestore called, Then returns list of runs`() =
        runTest {
            // Given
            every { user.uid } returns TEST_USER_ID
            every { auth.currentUser } returns user

            val runFirestoreModel =
                RunFirestoreModel(id = "1", distanceInMeters = 1000.0, timestamp = 123L)
            val run = runFirestoreModel.toRun()

            every { firestore.collection(TEST_RUNS_COLLECTION) } returns collectionRef
            every { collectionRef.whereEqualTo(TEST_USER_ID_FIELD, TEST_USER_ID) } returns query
            every { query.orderBy(TEST_TIMESTAMP_FIELD, Query.Direction.DESCENDING) } returns query

            val listenerSlot = slot<EventListener<QuerySnapshot>>()

            every { query.addSnapshotListener(capture(listenerSlot)) } answers {
                listenerSlot.captured.onEvent(
                    mockk<QuerySnapshot> {
                        every { documents } returns listOf(documentSnapshot)
                    },
                    null
                )
                listenerRegistration
            }

            every { documentSnapshot.toObject(RunFirestoreModel::class.java) } returns runFirestoreModel

            // When
            val result = runRemoteRepository.getAllRunsFromFirestore().first()

            // Then
            assertEquals(listOf(run), result)

            verify { firestore.collection(TEST_RUNS_COLLECTION) }
            verify { collectionRef.whereEqualTo(TEST_USER_ID_FIELD, TEST_USER_ID) }
            verify { query.orderBy(TEST_TIMESTAMP_FIELD, Query.Direction.DESCENDING) }
            verify { query.addSnapshotListener(any<EventListener<QuerySnapshot>>()) }
        }

    @Test
    fun `Given valid runId, When getRunByIdFromFirestore called, Then returns the run`() = runTest {
        // Given
        val runFirestoreModel =
            RunFirestoreModel(id = TEST_RUN_ID, distanceInMeters = 1000.0, timestamp = 12345L)
        val run = runFirestoreModel.toRun()

        every { firestore.collection(TEST_RUNS_COLLECTION) } returns collectionRef
        every { collectionRef.document(TEST_RUN_ID) } returns documentRef

        val listenerSlot = slot<EventListener<DocumentSnapshot>>()
        every { documentRef.addSnapshotListener(capture(listenerSlot)) } answers {
            listenerSlot.captured.onEvent(
                mockk<DocumentSnapshot> {
                    every { toObject(RunFirestoreModel::class.java) } returns runFirestoreModel
                },
                null
            )
            listenerRegistration
        }

        // When
        val result = runRemoteRepository.getRunByIdFromFirestore(TEST_RUN_ID).first()

        // Then
        assertEquals(run, result)

        verify { firestore.collection(TEST_RUNS_COLLECTION) }
        verify { collectionRef.document(TEST_RUN_ID) }
        verify { documentRef.addSnapshotListener(any<EventListener<DocumentSnapshot>>()) }
    }

    @Test
    fun `Given valid time range, When getThisWeekDistanceByDay called, Then returns list of RunChart`() =
        runTest {
            // Given
            every { auth.currentUser } returns user
            every { user.uid } returns TEST_USER_ID

            val start = Instant.parse("2025-09-01T00:00:00Z")
            val end = Instant.parse("2025-09-07T23:59:59Z")

            val runDoc = mockk<DocumentSnapshot> {
                every { getDouble(TEST_DISTANCE_IN_METERS_FIELD) } returns 1500.0
                every { getLong(TEST_TIMESTAMP_FIELD) } returns 123456789L
            }

            val snapshot = mockk<QuerySnapshot> {
                every { documents } returns listOf(runDoc)
            }

            val getTask: Task<QuerySnapshot> = Tasks.forResult(snapshot)

            every { firestore.collection(TEST_RUNS_COLLECTION) } returns collectionRef
            every { collectionRef.whereEqualTo(TEST_USER_ID_FIELD, TEST_USER_ID) } returns query
            every {
                query.whereGreaterThanOrEqualTo(
                    TEST_TIMESTAMP_FIELD,
                    start.toEpochMilli()
                )
            } returns query
            every {
                query.whereLessThanOrEqualTo(
                    TEST_TIMESTAMP_FIELD,
                    end.toEpochMilli()
                )
            } returns query
            every { query.get() } returns getTask

            // When
            val result = runRemoteRepository.getThisWeekDistanceByDay(start, end)

            // Then
            assertEquals(1, result.size)
            assertEquals(1500f, result[0].distanceMeters)
            assertEquals(123456789L, result[0].timestamp)

            verify { firestore.collection(TEST_RUNS_COLLECTION) }
            verify { collectionRef.whereEqualTo(TEST_USER_ID_FIELD, TEST_USER_ID) }
            verify { query.whereGreaterThanOrEqualTo(TEST_TIMESTAMP_FIELD, start.toEpochMilli()) }
            verify { query.whereLessThanOrEqualTo(TEST_TIMESTAMP_FIELD, end.toEpochMilli()) }
            verify { query.get() }
        }

    @Test
    fun `Given user with runs, When getTotalDistance called, Then returns sum of distances`() =
        runTest {
            // Given
            every { auth.currentUser } returns user
            every { user.uid } returns TEST_USER_ID

            val runDoc1 = mockk<DocumentSnapshot> {
                every { getDouble(TEST_DISTANCE_IN_METERS_FIELD) } returns 1000.0
            }

            val runDoc2 = mockk<DocumentSnapshot> {
                every { getDouble(TEST_DISTANCE_IN_METERS_FIELD) } returns 2000.0
            }

            val snapshot = mockk<QuerySnapshot> {
                every { documents } returns listOf(runDoc1, runDoc2)
            }

            val getTask: Task<QuerySnapshot> = Tasks.forResult(snapshot)

            every { firestore.collection(TEST_RUNS_COLLECTION) } returns collectionRef
            every { collectionRef.whereEqualTo(TEST_USER_ID_FIELD, TEST_USER_ID) } returns query
            every { query.get() } returns getTask

            // When
            val result = runRemoteRepository.getTotalDistance()

            // Then
            assertEquals(3000.0, result, 0.0)

            verify { firestore.collection(TEST_RUNS_COLLECTION) }
            verify { collectionRef.whereEqualTo(TEST_USER_ID_FIELD, TEST_USER_ID) }
            verify { query.get() }
        }
}
