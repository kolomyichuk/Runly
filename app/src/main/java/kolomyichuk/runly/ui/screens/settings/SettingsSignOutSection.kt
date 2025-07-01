package kolomyichuk.runly.ui.screens.settings

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import kolomyichuk.runly.ui.components.SignOutButton

@Composable
fun SettingsSignOutSection(
    signOutResult: Result<Unit>?,
    onSignOutClick: () -> Unit,
    onNavigateToSignIn: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(signOutResult) {
        signOutResult?.let { result ->
            if (result.isSuccess) {
                onNavigateToSignIn()
            } else {
                val message = result.exceptionOrNull()?.message ?: "Sign out failed"
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    SignOutButton(onSignOutClick = onSignOutClick)
}