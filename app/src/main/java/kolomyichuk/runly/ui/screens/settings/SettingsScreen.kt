package kolomyichuk.runly.ui.screens.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import kolomyichuk.runly.R
import kolomyichuk.runly.ui.components.TopBarApp

@Composable
fun SettingsScreen(
    navController: NavController,
    settingsViewModel: SettingsViewModel
) {
    Scaffold(
        topBar = {
            TopBarApp(
                title = stringResource(R.string.settings),
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        SettingsScreenContent(
            navController = navController,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            settingsViewModel = settingsViewModel,
        )
    }
}
