package kolomyichuk.runly.data.repository

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class AuthRepository(
    private val firebaseAuth: FirebaseAuth,
    @ApplicationContext private val context: Context
) {
    private val credentialManager by lazy {
        CredentialManager.create(context)
    }

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
        } catch (e: Exception) {
            Timber.e("General error: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun signOut(): Result<Unit> {
        return try {
            firebaseAuth.signOut()

            try {
                credentialManager.clearCredentialState(ClearCredentialStateRequest())
            } catch (e: Exception) {
                Timber.e("Could not clear credential state: ${e.message}")
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Timber.e("Sign out error: ${e.message}")
            Result.failure(e)
        }
    }
}