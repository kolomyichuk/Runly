package kolomyichuk.runly.ui.screens.run

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kolomyichuk.runly.utils.TrackingUtility

@Composable
fun RunScreenContent(
    isDarkTheme: Boolean,
    navController: NavController,
    runViewModel: RunViewModel
) {
    val runState by runViewModel.runState.collectAsStateWithLifecycle()
    val distance by remember(runState.distanceInMeters) {
        derivedStateOf { TrackingUtility.formatDistanceToKm(runState.distanceInMeters) }
    }
    val time by remember(runState.timeInMillis) {
        derivedStateOf { TrackingUtility.formatTime(runState.timeInMillis) }
    }
    val avgSpeed by remember(runState.avgSpeed) {
        derivedStateOf { runState.avgSpeed.toString() }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        RunMapView(
            isDarkTheme = isDarkTheme,
            modifier = Modifier.weight(1f),
            isTracking = runState.isTracking,
            pathPoints = runState.pathPoints
        )
        RunInfoBlock(
            distance = distance,
            time = time,
            avgSpeed = avgSpeed
        )
        RunStartBlock(
            isTracking = runState.isTracking,
            isPause = runState.isPause,
            navController = navController
        )
    }
}