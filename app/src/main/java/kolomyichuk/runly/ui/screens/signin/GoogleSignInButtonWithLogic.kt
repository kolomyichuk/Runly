package kolomyichuk.runly.ui.screens.signin

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kolomyichuk.runly.ui.components.GoogleSignInButton
import kotlinx.coroutines.launch
import timber.log.Timber

private const val CLIENT_ID =
    "729943515695-3kfe6qc1jhvd42r681opknat0762s50j.apps.googleusercontent.com"

@Composable
fun GoogleSignInButtonWithLogic(
    modifier: Modifier = Modifier,
    signInViewModel: SignInViewModel,
    onSignInSuccess: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val activity = LocalActivity.current as Activity

    GoogleSignInButton(
        onSignInClick = {
            coroutineScope.launch {
                try {
                    val credentialManager = CredentialManager.create(activity)
                    val googleIdOption = GetGoogleIdOption.Builder()
                        .setServerClientId(CLIENT_ID)
                        .setFilterByAuthorizedAccounts(false)
                        .build()

                    val request = GetCredentialRequest.Builder()
                        .addCredentialOption(googleIdOption)
                        .build()

                    val result = credentialManager.getCredential(activity, request)
                    val credential =
                        GoogleIdTokenCredential.createFrom(result.credential.data)
                    val idToken = credential.idToken

                    signInViewModel.signInWithGoogle(idToken)
                    onSignInSuccess()
                } catch (e: NoCredentialException) {
                    Timber.e("No Google accounts ${e.message}")
                } catch (e: GetCredentialCancellationException) {
                    Timber.d("User canceled")
                } catch (e: Exception) {
                    Timber.e("Authorization error ${e.message}")
                }
            }
        },
        modifier = modifier
    )
}