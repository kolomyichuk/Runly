package kolomyichuk.runly.ui.screens.signin

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kolomyichuk.runly.R
import kolomyichuk.runly.ui.components.GoogleSignInButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

private const val CLIENT_ID =
    "729943515695-3kfe6qc1jhvd42r681opknat0762s50j.apps.googleusercontent.com"

@Composable
fun SignInScreen(
    signInViewModel: SignInViewModel,
    onSignInSuccess: () -> Unit
) {
    Scaffold { innerPadding ->
        SignInScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            signInViewModel = signInViewModel,
            onSignInSuccess = onSignInSuccess
        )
    }
}


@Composable
fun SignInScreenContent(
    modifier: Modifier = Modifier,
    signInViewModel: SignInViewModel,
    onSignInSuccess: () -> Unit
) {
    val activity = LocalActivity.current as Activity

    Box(
        modifier = modifier.padding(16.dp),
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.join_us),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }

        GoogleSignInButton(
            onSignInClick = {
                CoroutineScope(Dispatchers.Main).launch {
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
            modifier = Modifier
                .padding(bottom = 32.dp)
                .align(Alignment.BottomCenter)
        )
    }
}