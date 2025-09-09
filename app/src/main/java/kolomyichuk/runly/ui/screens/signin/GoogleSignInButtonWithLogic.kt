@file:Suppress("DEPRECATION")

package kolomyichuk.runly.ui.screens.signin

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.EntryPointAccessors
import kolomyichuk.runly.di.GoogleSignInHelperEntryPoint
import kolomyichuk.runly.ui.components.GoogleSignInButton
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun GoogleSignInButtonWithLogic(
    onSignInSuccess: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val activity = LocalActivity.current as Activity
    val googleSignInHelper = rememberGoogleSignInHelper()

    var isSigningIn by remember { mutableStateOf(false) }

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        isSigningIn = false
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account.idToken?.let {
                googleSignInHelper.signInWithFirebase(it, onSignInSuccess)
            }
        } catch (e: ApiException) {
            Timber.e("Google sign-in failed: ${e.statusCode} (${e.message})")
        }
    }

    GoogleSignInButton(
        onSignInClick = {
            if (isSigningIn) return@GoogleSignInButton
            isSigningIn = true
            coroutineScope.launch {
                val idToken = googleSignInHelper.getIdTokenWithCredentialManager(activity)
                if (idToken != null) {
                    googleSignInHelper.signInWithFirebase(idToken, onSignInSuccess)
                } else {
                    googleSignInHelper.launchGoogleSignIn(activity, googleSignInLauncher)
                }
            }
        }
    )
}

@Composable
private fun rememberGoogleSignInHelper(): GoogleSignInHelper {
    val context = LocalContext.current.applicationContext
    return remember {
        EntryPointAccessors.fromApplication(
            context,
            GoogleSignInHelperEntryPoint::class.java
        ).googleSignInHelper()
    }
}
