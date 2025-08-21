package kolomyichuk.runly.data.repository

import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import kotlin.coroutines.cancellation.CancellationException

class AuthRepository(
    private val firebaseAuth: FirebaseAuth,
    private val credentialManager: CredentialManager
) {
    fun isUserSignedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    suspend fun signInWithGoogle(idToken: String): Result<Unit> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            firebaseAuth.signInWithCredential(credential).await()
            Result.success(Unit)
        } catch (e: FirebaseAuthException) {
            Timber.e("Firebase error: ${e.errorCode} - ${e.message}")
            Result.failure(e)
        } catch (e: IllegalArgumentException) {
            Timber.e("Invalid token: ${e.message}")
            Result.failure(e)
        } catch (e: CancellationException) {
            Timber.d("Sign-in was cancelled: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun signOut(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
            Result.success(Unit)
        } catch (e: SecurityException) {
            Timber.e("No permission to clear credentials: ${e.message}")
            Result.success(Unit)
        } catch (e: IllegalStateException) {
            Timber.e("CredentialManager in invalid state: ${e.message}")
            Result.success(Unit)
        }
    }
}
