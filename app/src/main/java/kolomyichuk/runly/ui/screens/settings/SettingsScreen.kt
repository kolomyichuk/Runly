package kolomyichuk.runly.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kolomyichuk.runly.R
import kolomyichuk.runly.ui.components.HorizontalLineDivider
import kolomyichuk.runly.ui.components.TopBarApp
import kolomyichuk.runly.ui.navigation.Screen

@Composable
fun SettingsScreen(
    navController: NavController
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
                .padding(innerPadding)
        )
    }
}

@Composable
private fun SettingsScreenContent(
    navController: NavController,
    modifier: Modifier = Modifier
) {
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
    }
}