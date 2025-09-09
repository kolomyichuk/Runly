@file:Suppress("DEPRECATION")

package kolomyichuk.runly.ui.screens.signin

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kolomyichuk.runly.R
import timber.log.Timber

class GoogleSignInHelper(
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun getIdTokenWithCredentialManager(activity: Activity): String? {
        return try {
            val credentialManager = CredentialManager.create(activity)
            val googleIdOption = GetGoogleIdOption.Builder()
                .setServerClientId(activity.getString(R.string.default_web_client_id))
                .setFilterByAuthorizedAccounts(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(activity, request)
            val credential = GoogleIdTokenCredential.createFrom(result.credential.data)
            credential.idToken.takeIf { it.isNotBlank() }
        } catch (e: GetCredentialException) {
            Timber.e(e, "CredentialManager failed")
            null
        }
    }

    fun launchGoogleSignIn(activity: Activity, launcher: ActivityResultLauncher<Intent>) {
        val webClientId = activity.getString(R.string.default_web_client_id)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()
        val googleClient = GoogleSignIn.getClient(activity, gso)
        launcher.launch(googleClient.signInIntent)
    }

    fun signInWithFirebase(idToken: String, onSuccess: (String) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { res ->
                if (res.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    if (user != null) onSuccess(user.uid)
                    else Timber.e("User is null after Firebase sign-in")
                } else {
                    Timber.e("Firebase sign-in failed: ${res.exception?.message}")
                }
            }
    }
}
