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
import kolomyichuk.runly.domain.run.model.RoutePoint
import kolomyichuk.runly.ui.ext.toLatLng

const val MAP_ZOOM = 15f
private const val DEFAULT_CAMERA_LOCATION_LATITUDE = 49.010708
private const val DEFAULT_CAMERA_LOCATION_LONGITUDE = 25.796191

@Composable
fun RunMapView(
    hasForegroundLocationPermission: Boolean,
    isTracking: Boolean,
    pathPoints: List<List<RoutePoint>>,
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
        locationCallback = locationCallback,
        hasForegroundLocationPermission = hasForegroundLocationPermission
    )

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            if (pathPoints.isNotEmpty() && pathPoints.first().isNotEmpty()
            ) pathPoints.first().first().toLatLng()
            else LatLng(DEFAULT_CAMERA_LOCATION_LATITUDE, DEFAULT_CAMERA_LOCATION_LONGITUDE),
            MAP_ZOOM
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
        pathPoints = pathPoints,
        currentLocation = currentLocation,
        cameraPositionState = cameraPositionState
    )
}
