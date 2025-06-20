package kolomyichuk.runly.ui.screens.signin

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
import kolomyichuk.runly.R

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

        GoogleSignInButtonWithLogic(
            modifier = Modifier
                .padding(bottom = 32.dp)
                .align(Alignment.BottomCenter),
            signInViewModel = signInViewModel,
            onSignInSuccess = onSignInSuccess
        )
    }
}