package kolomyichuk.runly.ui.screens.run

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import kolomyichuk.runly.domain.run.model.RoutePoint
import kolomyichuk.runly.ui.components.MapTypeToggleButton

const val POLYLINE_WIDTH = 12f

@Composable
fun MapContent(
    modifier: Modifier,
    isTracking: Boolean,
    pathPoints: List<List<RoutePoint>>,
    currentLocation: LatLng?,
    cameraPositionState: CameraPositionState
) {
    var isSatellite by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        MapDisplay(
            isTracking = isTracking,
            pathPoints = pathPoints,
            currentLocation = currentLocation,
            cameraPositionState = cameraPositionState,
            isSatellite = isSatellite
        )
        MapTypeToggleButton(
            modifier = Modifier.align(Alignment.TopEnd),
            onToggle = { isSatellite = !isSatellite }
        )
    }
}
