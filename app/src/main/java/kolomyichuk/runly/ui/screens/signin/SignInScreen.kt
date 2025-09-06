package kolomyichuk.runly.ui.screens.signin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SignInScreen(
    onSignInSuccess: (String) -> Unit
) {
    Scaffold { innerPadding ->
        SignInScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            onSignInSuccess = onSignInSuccess
        )
    }
}


@Composable
private fun SignInScreenContent(
    modifier: Modifier = Modifier,
    onSignInSuccess: (String) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SignInWelcomeHeader()
        }

        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GoogleSignInButtonWithLogic(
                onSignInSuccess = onSignInSuccess,
                modifier = Modifier.padding(8.dp)
            )

            SignInTermsAndPrivacy()
        }
    }
}
