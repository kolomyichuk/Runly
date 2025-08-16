package kolomyichuk.runly.ui.screens.run

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.LatLng

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RunScreenContent(
    navController: NavController,
    runViewModel: RunViewModel
) {
    val runDisplayState by runViewModel.runDisplayState.collectAsStateWithLifecycle()

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
            isTracking = runDisplayState.isTracking,
            pathPoints = runDisplayState.routePoints,
            hasForegroundLocationPermission = foregroundPermissionState.allPermissionsGranted
        )
        RunInfoBlock(
            distance = runDisplayState.distance,
            time = runDisplayState.duration,
            avgSpeed = runDisplayState.avgSpeed,
            distanceUnit = runDisplayState.unit
        )
        RunStartBlock(
            isTracking = runDisplayState.isTracking,
            isPause = runDisplayState.isPause,
            navController = navController,
            hasForegroundLocationPermission = foregroundPermissionState.allPermissionsGranted,
            onSaveRun = { runViewModel.saveRun() },
            distance = runDisplayState.distance
        )
    }
}
