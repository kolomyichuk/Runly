package kolomyichuk.runly.ui.screens.settings

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import kolomyichuk.runly.ui.components.SignOutButton

@Composable
fun SettingsSignOutSection(
    signOutSuccess: Boolean,
    signOutError: String?,
    onSignOutClick: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    onResetStates: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(signOutSuccess) {
        if (signOutSuccess){
            onNavigateToSignIn()
            onResetStates()
        }
    }

    LaunchedEffect(signOutError) {
        signOutError?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            onResetStates()
        }
    }

    SignOutButton(onSignOutClick = onSignOutClick)
}