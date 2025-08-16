package kolomyichuk.runly.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kolomyichuk.runly.R
import kolomyichuk.runly.ui.components.TopBarApp
import kolomyichuk.runly.ui.screens.run.RunViewModel

@Composable
fun DashboardScreen(
    navController: NavController,
    runViewModel: RunViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopBarApp(
            title = stringResource(R.string.dashboard),
            onBackClick = { navController.popBackStack() }
        )
        ContentDashboardScreen(navController = navController, runViewModel = runViewModel)
    }
}

@Composable
private fun ContentDashboardScreen(
    navController: NavController,
    runViewModel: RunViewModel
) {
    val runDisplayState by runViewModel.runDisplayState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        DashboardInfoBlock(
            time = runDisplayState.duration,
            distance = runDisplayState.distance,
            avgSpeed = runDisplayState.avgSpeed,
            distanceUnit = runDisplayState.unit
        )
        DashboardButtonsBlock(
            navController = navController,
            isTracking = runDisplayState.isTracking,
            isActiveRun = runDisplayState.isActiveRun,
            distance = runDisplayState.distance,
            onSaveRun = { runViewModel.saveRun() }
        )
    }
}
