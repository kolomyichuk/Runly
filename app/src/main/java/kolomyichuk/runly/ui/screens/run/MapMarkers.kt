package kolomyichuk.runly.ui.screens.run

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import kolomyichuk.runly.domain.run.model.RoutePoint
import kolomyichuk.runly.ui.components.currentLocationMarker
import kolomyichuk.runly.ui.ext.toLatLng

@Composable
fun MapMarkers(
    isTracking: Boolean,
    pathPoints: List<List<RoutePoint>>,
    currentLocation: LatLng?
) {
    if (!isTracking && currentLocation != null) {
        Marker(
            state = MarkerState(currentLocation),
            icon = currentLocationMarker,
            anchor = Offset(x = 0.5f, y = 0.5f)
        )
    }

    pathPoints.forEach { segment ->
        if (segment.isNotEmpty()) {
            Polyline(
                points = segment.map { it.toLatLng() },
                color = MaterialTheme.colorScheme.primary,
                width = POLYLINE_WIDTH
            )
        }
    }

    if (isTracking) {
        val lastSegment = pathPoints.lastOrNull()
        lastSegment?.lastOrNull()?.let { latestLocation ->
            Marker(
                state = MarkerState(latestLocation.toLatLng()),
                icon = currentLocationMarker,
                anchor = Offset(x = 0.5f, y = 0.5f)
            )
        }
    }
}
