package kolomyichuk.runly.ui.screens.signin

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kolomyichuk.runly.R
import kolomyichuk.runly.ui.components.GoogleSignInButton
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun GoogleSignInButtonWithLogic(
    onSignInSuccess: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val activity = LocalActivity.current as Activity

    GoogleSignInButton(
        onSignInClick = {
            coroutineScope.launch {
                try {
                    val credentialManager = CredentialManager.create(activity)
                    val googleIdOption = GetGoogleIdOption.Builder()
                        .setServerClientId(activity.getString(R.string.default_web_client_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build()

                    val request = GetCredentialRequest.Builder()
                        .addCredentialOption(googleIdOption)
                        .build()

                    val result = credentialManager.getCredential(activity, request)
                    val credential =
                        GoogleIdTokenCredential.createFrom(result.credential.data)
                    val idToken = credential.idToken

                    onSignInSuccess(idToken)
                } catch (e: NoCredentialException) {
                    Timber.e(e, "No Google accounts")
                } catch (e: GetCredentialCancellationException) {
                    Timber.d(e, "User canceled")
                } catch (e: IllegalArgumentException) {
                    Timber.e(e, "Invalid credentials")
                }
            }
        },
        modifier = Modifier.padding(8.dp)
    )
}
