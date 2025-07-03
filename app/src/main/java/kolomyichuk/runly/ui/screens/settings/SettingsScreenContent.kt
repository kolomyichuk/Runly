package kolomyichuk.runly.ui.screens.settings

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kolomyichuk.runly.R
import kolomyichuk.runly.ui.components.HorizontalLineDivider
import kolomyichuk.runly.ui.components.SignOutButton
import kolomyichuk.runly.ui.navigation.Screen

@Composable
fun SettingsScreenContent(
    navController: NavController,
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel,
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        settingsViewModel.signOutEffect.collect { effect ->
            when (effect) {
                is SignOutEffect.Success -> {
                    navController.navigate(Screen.SignIn) {
                        popUpTo(0) { inclusive = true }
                    }
                }

                is SignOutEffect.Failure -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.sign_out_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    Column(
        modifier = modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        SettingsItem(
            label = stringResource(R.string.theme),
            onNavigateToSettingsItem = {
                navController.navigate(Screen.Theme)
            },
            settingsItemIcon = R.drawable.theme_mode
        )

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalLineDivider()
        Spacer(modifier = Modifier.height(8.dp))

        SettingsItem(
            label = stringResource(R.string.units_of_measure),
            onNavigateToSettingsItem = {
                navController.navigate(Screen.UnitsOfMeasure)
            },
            settingsItemIcon = R.drawable.measure
        )

        Spacer(modifier = Modifier.weight(1f))

        SignOutButton(onSignOutClick = { settingsViewModel.signOut() })
    }
}