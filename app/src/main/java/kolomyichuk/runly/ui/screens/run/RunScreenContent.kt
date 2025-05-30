package kolomyichuk.runly.ui.screens.run

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kolomyichuk.runly.utils.FormatterUtils

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RunScreenContent(
    navController: NavController,
    runViewModel: RunViewModel
) {
    val runState by runViewModel.runState.collectAsStateWithLifecycle()
    val distance by remember(runState.distanceInMeters) {
        derivedStateOf { FormatterUtils.formatDistanceToKm(runState.distanceInMeters) }
    }
    val time by remember(runState.timeInMillis) {
        derivedStateOf { FormatterUtils.formatTime(runState.timeInMillis) }
    }
    val avgSpeed by remember(runState.avgSpeed) {
        derivedStateOf { runState.avgSpeed.toString() }
    }

    val foregroundPermissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    HandleForegroundPermissions(foregroundPermissionState = foregroundPermissionState)

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        RunMapView(
            modifier = Modifier.weight(1f),
            isTracking = runState.isTracking,
            pathPoints = runState.pathPoints,
            hasForegroundLocationPermission = foregroundPermissionState.allPermissionsGranted
        )
        RunInfoBlock(
            distance = distance,
            time = time,
            avgSpeed = avgSpeed
        )
        RunStartBlock(
            isTracking = runState.isTracking,
            isPause = runState.isPause,
            navController = navController,
            hasForegroundLocationPermission = foregroundPermissionState.allPermissionsGranted,
            onSaveRun = { runViewModel.saveRun() },
            distance = runState.distanceInMeters
        )
    }
}