package kolomyichuk.runly.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kolomyichuk.runly.R
import kolomyichuk.runly.ui.components.TopBarApp
import kolomyichuk.runly.ui.screens.run.RunViewModel
import kolomyichuk.runly.utils.FormatterUtils

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
    val runState by runViewModel.runState.collectAsStateWithLifecycle()
    val time by remember(runState.timeInMillis) {
        derivedStateOf { FormatterUtils.formatTime(runState.timeInMillis) }
    }
    val distance by remember(runState.distanceInMeters) {
        derivedStateOf { FormatterUtils.formatDistanceToKm(runState.distanceInMeters) }
    }
    val avgSpeed by remember(runState.avgSpeed) {
        derivedStateOf { runState.avgSpeed.toString() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        DashboardInfoBlock(
            time = time,
            distance = distance,
            avgSpeed = avgSpeed
        )
        DashboardButtonsBlock(
            navController = navController,
            isTracking = runState.isTracking,
            isActiveRun = runState.isActiveRun,
            distance = runState.distanceInMeters,
            onSaveRun = { runViewModel.saveRun() }
        )
    }
}