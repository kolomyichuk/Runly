package kolomyichuk.runly.data.repository

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kolomyichuk.runly.data.remote.firestore.model.RunFirestoreModel
import kolomyichuk.runly.utils.createRun1
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

private const val RUNS_COLLECTION = "runs"

class RunRemoteRepositoryImplTest {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var runRemoteRepository: RunRemoteRepositoryImpl
    private lateinit var collectionRef: CollectionReference
    private lateinit var documentRef: DocumentReference
    private lateinit var user: FirebaseUser

    @Before
    fun setup() {
        auth = mockk<FirebaseAuth>(relaxed = true)
        firestore = mockk<FirebaseFirestore>(relaxed = true)
        runRemoteRepository = RunRemoteRepositoryImpl(auth = auth, firestore = firestore)
        collectionRef = mockk<CollectionReference>()
        documentRef = mockk<DocumentReference>()
        user = mockk<FirebaseUser>()
    }

    @After
    fun tearDown() {
        clearAllMocks()
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

            coEvery { firestore.collection(RUNS_COLLECTION) } returns collectionRef
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
        val runId = "testRunId"
        coEvery { firestore.collection(RUNS_COLLECTION).document(runId) } returns documentRef
        coEvery { documentRef.delete() } returns Tasks.forResult(null)

        // When
        runRemoteRepository.deleteRunByIdInFirestore(runId)

        // Then
        coVerify { documentRef.delete() }
    }

    @Test
    fun `Given runId, When delete fails, Then FirebaseFirestoreException is caught`() = runTest {
        // Given
        val runId = "test"
        val exception = mockk<FirebaseFirestoreException>(relaxed = true)
        coEvery { firestore.collection(RUNS_COLLECTION).document(runId) } returns documentRef
        coEvery { documentRef.delete() } returns Tasks.forException(exception)

        // When
        runRemoteRepository.deleteRunByIdInFirestore(runId)

        // Then
        verify { documentRef.delete() }
    }
}
