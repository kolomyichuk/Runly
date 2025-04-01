package kolomyichuk.runly.ui.screens

import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kolomyichuk.runly.R
import kolomyichuk.runly.data.local.datastore.AppTheme
import kolomyichuk.runly.service.RunTrackingService
import kolomyichuk.runly.ui.components.ButtonStart
import kolomyichuk.runly.ui.components.CircleIconButton
import kolomyichuk.runly.ui.components.TopBarApp
import kolomyichuk.runly.ui.components.currentLocationMarker
import kolomyichuk.runly.ui.viewmodel.ThemeViewModel
import kolomyichuk.runly.utils.Constants
import kolomyichuk.runly.utils.TrackingUtility
import kolomyichuk.runly.utils.pauseTrackingService
import kolomyichuk.runly.utils.resumeTrackingService
import kolomyichuk.runly.utils.startRunTrackingService
import kolomyichuk.runly.utils.stopTrackingService

@Composable
fun RunScreen(
    navController: NavController,
    themeViewModel: ThemeViewModel = hiltViewModel()
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopBarApp(
            title = stringResource(R.string.run),
            onBackClick = { navController.popBackStack() }
        )
        val theme by themeViewModel.themeFlow.collectAsState()

        val isDarkTheme = when (theme) {
            AppTheme.DARK -> true
            AppTheme.LIGHT -> false
            AppTheme.SYSTEM -> isSystemInDarkTheme()
        }
        ContentRunScreen(isDarkTheme = isDarkTheme)
    }
}

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun ContentRunScreen(isDarkTheme: Boolean) {
    val context = LocalContext.current
    val timeInMillisState by RunTrackingService.timeInMillis.collectAsState(0L)
    val isTracking by RunTrackingService.isTracking.collectAsState(initial = false)
    val isPause by RunTrackingService.isPause.collectAsState(initial = false)
    var hasNotificationPermission by remember { mutableStateOf(true) }
    val pathPoints by RunTrackingService.pathPoints.collectAsState(emptyList())
    val distanceInMeters by RunTrackingService.distanceInMeters.collectAsState(0.0)

    val mapStyleRes = if (isDarkTheme) {
        R.raw.map_night_style
    } else {
        R.raw.map_light_style
    }

    val permissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val locationGranted = permissions["android.permission.ACCESS_FINE_LOCATION"] == true &&
                permissions["android.permission.ACCESS_COARSE_LOCATION"] == true
        val notificationGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions["android.permission.POST_NOTIFICATIONS"] == true
        } else true

        hasNotificationPermission = notificationGranted
        if (locationGranted) {
            startRunTrackingService(context = context)
        }
    }

    val formattedDistance = "%.2f".format(distanceInMeters / 1000)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            if (pathPoints.isNotEmpty() && pathPoints.first().isNotEmpty()) pathPoints.first()
                .first()
            else LatLng(49.010708, 25.796191), Constants.MAP_ZOOM
        )
    }

    LaunchedEffect(pathPoints) {
        pathPoints.lastOrNull()?.lastOrNull()?.let { latestLocation ->
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(latestLocation, Constants.MAP_ZOOM),
                1000
            )
        }
    }

    val formattedTime = TrackingUtility.formatTime(timeInMillisState)

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier.weight(1f)
        ) {
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(zoomControlsEnabled = true, zoomGesturesEnabled = true)
            ) {
                MapEffect(isDarkTheme) { map ->
                    val mapStyle = MapStyleOptions.loadRawResourceStyle(context, mapStyleRes)
                    map.setMapStyle(mapStyle)
                }

                pathPoints.forEach { segment ->
                    if (segment.isNotEmpty()){
                        Polyline(
                            points = segment,
                            color = MaterialTheme.colorScheme.primary,
                            width = Constants.POLYLINE_WIDTH
                        )
                    }
                }

                val lastSegment = pathPoints.lastOrNull()
                if (lastSegment != null && lastSegment.isNotEmpty()) {
                    Marker(
                        state = MarkerState(lastSegment.last()),
                        icon = currentLocationMarker,
                        anchor = Offset(0.5f, 0.5f)
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            InfoColumn(formattedDistance, stringResource(R.string.distance))
            InfoColumn("--:-- /km", "Current Pace")
            InfoColumn(formattedTime, stringResource(R.string.time))
        }

        if (!isTracking && !isPause) {
            ButtonStart(
                onClick = {
                    val permissionsToRequest = mutableListOf<String>()

                    if (ContextCompat.checkSelfPermission(
                            context, "android.permission.ACCESS_FINE_LOCATION"
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        permissionsToRequest.add("android.permission.ACCESS_FINE_LOCATION")
                    }
                    if (ContextCompat.checkSelfPermission(
                            context, "android.permission.ACCESS_COARSE_LOCATION"
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        permissionsToRequest.add("android.permission.ACCESS_COARSE_LOCATION")
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                        ContextCompat.checkSelfPermission(
                            context, "android.permission.POST_NOTIFICATIONS"
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        permissionsToRequest.add("android.permission.POST_NOTIFICATIONS")
                    }

                    if (permissionsToRequest.isNotEmpty()) {
                        permissionsLauncher.launch(permissionsToRequest.toTypedArray())
                    } else {
                        startRunTrackingService(context = context)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, bottom = 16.dp),
                text = stringResource(R.string.start),
                roundDp = 8.dp
            )
        } else {
            ControlButtons(
                isTracking = isTracking,
                isPause = isPause,
                onPause = { pauseTrackingService(context = context) },
                onResume = { resumeTrackingService(context = context) },
                onStop = { stopTrackingService(context = context) }
            )
        }
    }
}


@Composable
fun InfoColumn(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 12.sp
        )
    }
}

@Composable
fun ControlButtons(
    isTracking: Boolean,
    isPause: Boolean,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onStop: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    )
    {
        if (isTracking || isPause) {
            CircleIconButton(
                onClick = { onStop() },
                imageVector = Icons.Filled.Stop,
                iconColor = MaterialTheme.colorScheme.onSurface,
                elevation = 10.dp,
                iconSize = 28.dp,
                buttonSize = 40.dp,
                backgroundColor = MaterialTheme.colorScheme.surface,
                contentDescription = stringResource(R.string.stop)
            )
        }

        Button(
            onClick = { if (isTracking) onPause() else onResume() },
            modifier = Modifier
                .width(130.dp)
                .height(40.dp)
        ) {
            Text(text = if (isTracking) stringResource(R.string.pause) else stringResource(R.string.resume))
        }

        CircleIconButton(
            onClick = {

            },
            imageVector = Icons.Outlined.LocationOn,
            iconColor = MaterialTheme.colorScheme.onSurface,
            elevation = 10.dp,
            iconSize = 28.dp,
            buttonSize = 40.dp,
            backgroundColor = MaterialTheme.colorScheme.surface,
            contentDescription = "Map view"
        )
    }
}
