package kolomyichuk.runly.ui.screens.run

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RunScreenContent(
    navController: NavController,
    runViewModel: RunViewModel
) {
    val runState by runViewModel.runDisplayState.collectAsStateWithLifecycle()

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
            pathPoints = runState.routePoints,
            hasForegroundLocationPermission = foregroundPermissionState.allPermissionsGranted
        )
        RunInfoBlock(
            distance = runState.distance,
            time = runState.duration,
            avgSpeed = runState.avgSpeed,
            distanceUnit = runState.unit
        )
        RunStartBlock(
            runState = runState,
            hasForegroundLocationPermission = foregroundPermissionState.allPermissionsGranted,
            navController = navController,
            onSaveRun = { runViewModel.saveRun() },
        )
    }
}
