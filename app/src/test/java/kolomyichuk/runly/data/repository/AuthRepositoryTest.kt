package kolomyichuk.runly.data.repository

import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.coroutines.cancellation.CancellationException

class AuthRepositoryTest {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var credentialManager: CredentialManager
    private lateinit var authRepository: AuthRepository

    @Before
    fun setup() {
        firebaseAuth = mockk(relaxed = true)
        credentialManager = mockk(relaxed = true)
        authRepository = AuthRepository(firebaseAuth, credentialManager)
    }

    @Test
    fun `Given mockUser, When isUserSignedIn is called, Then true should be returned`() {
        // Given
        val mockUser = mockk<FirebaseUser>(relaxed = true)
        every { firebaseAuth.currentUser } returns mockUser

        // When
        val result = authRepository.isUserSignedIn()

        // Then
        assertTrue(result)
    }

    @Test
    fun `Given no user is logged in, When isUserSignIn is called, Then it returns false`() {
        // Given
        every { firebaseAuth.currentUser } returns null

        // When
        val result = authRepository.isUserSignedIn()

        // Then
        assertFalse(result)
    }

    @Test
    fun `Given valid id token, When signInWithGoogle is called, Then it returns success`() =
        runTest {
            // Given
            val authCredential = mockk<AuthCredential>(relaxed = true)
            val authResult = mockk<AuthResult>(relaxed = true)
            val idToken = "valid_id_token"

            mockkStatic(GoogleAuthProvider::class)
            mockkStatic(Tasks::class)

            every { GoogleAuthProvider.getCredential(idToken, null) } returns authCredential
            every { firebaseAuth.signInWithCredential(authCredential) } returns Tasks.forResult(
                authResult
            )

            // When
            val result = authRepository.signInWithGoogle(idToken)

            // Then
            assertTrue(result.isSuccess)
            assertEquals(Unit, result.getOrNull())

            verify(exactly = 1) {
                GoogleAuthProvider.getCredential(idToken, null)
                firebaseAuth.signInWithCredential(authCredential)
            }

            unmockkStatic(GoogleAuthProvider::class)
            unmockkStatic(Tasks::class)
        }

    @Test
    fun `Given invalid id token, When signInWithGoogle is called, Then it returns failure withFirebaseAuthException`() =
        runTest {
            // Given
            val idToken = "invalid_token"
            val credential = mockk<AuthCredential>(relaxed = true)
            val task = mockk<Task<AuthResult>>(relaxed = true)
            val exception = mockk<FirebaseAuthException>(relaxed = true)

            mockkStatic(GoogleAuthProvider::class)
            mockkStatic("kotlinx.coroutines.tasks.TasksKt")

            every { GoogleAuthProvider.getCredential(idToken, null) } returns credential
            every { firebaseAuth.signInWithCredential(credential) } returns task
            coEvery { task.await() } throws exception

            // When
            val result = authRepository.signInWithGoogle(idToken)

            // Then
            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is FirebaseAuthException)

            verify { GoogleAuthProvider.getCredential(idToken, null) }
            verify { firebaseAuth.signInWithCredential(credential) }
            coVerify { task.await() }

            unmockkStatic(GoogleAuthProvider::class)
            unmockkStatic("kotlinx.coroutines.tasks.TasksKt")
        }

    @Test
    fun `Given an illegal argument, When signInWithGoogle is called, Then it returns failure with IllegalArgumentException`() =
        runTest {
            // Given
            val idToken = ""
            val exception = IllegalArgumentException("Token must not be empty")

            mockkStatic(GoogleAuthProvider::class)

            every { GoogleAuthProvider.getCredential(idToken, null) } throws exception

            // When
            val result = authRepository.signInWithGoogle(idToken)

            // Then
            assertTrue(result.isFailure)
            val thrown = result.exceptionOrNull()
            assertTrue(thrown is IllegalArgumentException)
            assertEquals("Token must not be empty", thrown?.message)

            unmockkStatic(GoogleAuthProvider::class)
        }

    @Test
    fun `Given coroutine is cancelled, When signInWithGoogle is called, Then it returns failure with CancellationException`() =
        runTest {
            // Given
            val idToken = "some_token"
            val authCredential = mockk<AuthCredential>(relaxed = true)
            val task = mockk<Task<AuthResult>>(relaxed = true)

            mockkStatic(GoogleAuthProvider::class)
            mockkStatic("kotlinx.coroutines.tasks.TasksKt")

            every { GoogleAuthProvider.getCredential(idToken, null) } returns authCredential
            every { firebaseAuth.signInWithCredential(authCredential) } returns task
            coEvery { task.await() } throws CancellationException("Coroutine cancelled")

            // When
            val result = authRepository.signInWithGoogle(idToken)

            // Then
            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is CancellationException)

            unmockkStatic(GoogleAuthProvider::class)
            unmockkStatic("kotlinx.coroutines.tasks.TasksKt")
        }


    @Test
    fun `Given user is signed in, When signOut is called, Then FirebaseAuth signs out and credentials are cleared`() =
        runTest {
            // Given
            coEvery { credentialManager.clearCredentialState(any()) } just Runs

            // When
            val result = authRepository.signOut()

            // Then
            verify { firebaseAuth.signOut() }
            coVerify { credentialManager.clearCredentialState(any<ClearCredentialStateRequest>()) }
            assertTrue(result.isSuccess)
        }

    @Test
    fun `Given clearCredentialState throws SecurityException, When signOut is called, Then it still returns success`() =
        runTest {
            // Given
            coEvery { credentialManager.clearCredentialState(any()) } throws
                    SecurityException("No permission")

            // When
            val result = authRepository.signOut()

            // Then
            verify { firebaseAuth.signOut() }
            coVerify { credentialManager.clearCredentialState(any<ClearCredentialStateRequest>()) }
            assertTrue(result.isSuccess)
        }

    @Test
    fun `Given clearCredentialState throws IllegalStateException, When signOut is called, Then it still returns success`() =
        runTest {
            // Given
            coEvery { credentialManager.clearCredentialState(any()) } throws
                    IllegalStateException("CredentialManager in invalid state")

            // When
            val result = authRepository.signOut()

            // Then
            verify { firebaseAuth.signOut() }
            coVerify { credentialManager.clearCredentialState(any<ClearCredentialStateRequest>()) }
            assertTrue(result.isSuccess)
        }
}
