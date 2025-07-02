package kolomyichuk.runly.ui.screens.settings

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import kolomyichuk.runly.ui.components.SignOutButton
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun SettingsSignOutSection(
    signOutEffect: SharedFlow<SignOutEffect>,
    onSignOutClick: () -> Unit,
    onNavigateToSignIn: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        signOutEffect.collect { effect ->
            when (effect) {
                is SignOutEffect.Success -> onNavigateToSignIn()
                is SignOutEffect.Failure -> {
                    Toast.makeText(context, effect.messageResId, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    SignOutButton(onSignOutClick = onSignOutClick)
}