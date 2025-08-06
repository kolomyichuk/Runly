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
    fun `isUserSignedIn returns true when user exists`() {
        // Given
        val mockUser = mockk<FirebaseUser>(relaxed = true)
        every { firebaseAuth.currentUser } returns mockUser

        // When
        val result = authRepository.isUserSignedIn()

        // Then
        assertTrue(result)
    }

    @Test
    fun `isUserSignedIn returns false when user is null`() {
        // Given
        every { firebaseAuth.currentUser } returns null

        // When
        val result = authRepository.isUserSignedIn()

        // Then
        assertFalse(result)
    }

    @Test
    fun `signInWithGoogle should return success when authentication succeeds`() = runTest {
        // Given
        val authCredential = mockk<AuthCredential>(relaxed = true)
        val authResult = mockk<AuthResult>(relaxed = true)
        val idToken = "valid_id_token"

        mockkStatic(GoogleAuthProvider::class)
        mockkStatic(Tasks::class)

        every { GoogleAuthProvider.getCredential(idToken, null) } returns authCredential
        every { firebaseAuth.signInWithCredential(authCredential) } returns Tasks.forResult(authResult)

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
    fun `signInWithGoogle should return failure when FirebaseAuthException occurs`() = runTest {
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
    fun `signInWithGoogle should return failure when general Exception occurs`() = runTest {
        // Given
        val authCredential = mockk<AuthCredential>(relaxed = true)
        val generalException = Exception("Network error")
        val idToken = "some_token"

        mockkStatic(GoogleAuthProvider::class)
        mockkStatic(Tasks::class)

        every { GoogleAuthProvider.getCredential(idToken, null) } returns authCredential
        every { firebaseAuth.signInWithCredential(authCredential) } returns Tasks.forException(generalException)

        // When
        val result = authRepository.signInWithGoogle(idToken)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is Exception)
        assertEquals("Network error", exception?.message)

        unmockkStatic(GoogleAuthProvider::class)
        unmockkStatic(Tasks::class)
    }

    @Test
    fun `signOut calls FirebaseAuth signOut and clears credentials`() = runTest {
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
    fun `signOut returns failure when Firebase signOut throws exception`() = runTest {
        // Given
        val exception = Exception("Logout failed")
        every { firebaseAuth.signOut() } throws exception

        // When
        val result = authRepository.signOut()

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `signOut still succeeds if clearCredentialsState fails`() = runTest {
        // Given
        val exception = Exception("Clear failed")
        coEvery { credentialManager.clearCredentialState(any()) } throws exception

        // When
        val result = authRepository.signOut()

        // Then
        verify { firebaseAuth.signOut() }
        coVerify { credentialManager.clearCredentialState(any<ClearCredentialStateRequest>()) }

        assertTrue(result.isSuccess)
    }
}