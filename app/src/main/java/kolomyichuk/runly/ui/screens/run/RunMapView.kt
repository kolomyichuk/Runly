package kolomyichuk.runly.ui.screens.run

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState

const val MAP_ZOOM = 15f

@Composable
fun RunMapView(
    isTracking: Boolean,
    pathPoints: List<List<LatLng>>,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    var locationCallback by remember { mutableStateOf<LocationCallback?>(null) }

    HandlePassiveLocationUpdates(
        isTracking = isTracking,
        fusedLocationClient = fusedLocationClient,
        onLocationUpdate = { currentLocation = it },
        onCallbackChanged = { locationCallback = it },
        locationCallback = locationCallback
    )

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            if (pathPoints.isNotEmpty() && pathPoints.first().isNotEmpty()
            ) pathPoints.first().first()
            else LatLng(49.010708, 25.796191), MAP_ZOOM
        )
    }

    AnimateCameraOnMap(
        isTracking = isTracking,
        currentLocation = currentLocation,
        pathPoints = pathPoints,
        cameraPositionState = cameraPositionState
    )

    MapContent(
        modifier = modifier,
        isTracking = isTracking,
        isDarkTheme = isDarkTheme,
        pathPoints = pathPoints,
        currentLocation = currentLocation,
        cameraPositionState = cameraPositionState
    )
}