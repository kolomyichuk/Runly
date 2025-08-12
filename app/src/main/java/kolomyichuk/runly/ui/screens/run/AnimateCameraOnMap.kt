package kolomyichuk.runly.ui.screens.run

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import kolomyichuk.runly.domain.run.model.RoutePoint
import kolomyichuk.runly.ui.ext.toLatLng

private const val CAMERA_ANIMATION_DURATION = 1000

@Composable
fun AnimateCameraOnMap(
    isTracking: Boolean,
    currentLocation: LatLng?,
    pathPoints: List<List<RoutePoint>>,
    cameraPositionState: CameraPositionState
) {
    LaunchedEffect(isTracking, currentLocation, pathPoints) {
        if (currentLocation != null) {
            if (isTracking) {
                pathPoints.lastOrNull()?.lastOrNull()?.let { latestLocation ->
                    cameraPositionState.animate(
                        CameraUpdateFactory.newLatLngZoom(latestLocation.toLatLng(), MAP_ZOOM),
                        CAMERA_ANIMATION_DURATION
                    )
                }
            } else {
                currentLocation.let {
                    cameraPositionState.animate(
                        CameraUpdateFactory.newLatLngZoom(
                            it,
                            MAP_ZOOM
                        ), CAMERA_ANIMATION_DURATION
                    )
                }
            }
        }
    }
}