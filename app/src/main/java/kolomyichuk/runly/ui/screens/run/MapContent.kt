package kolomyichuk.runly.ui.screens.run

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import kolomyichuk.runly.R
import kolomyichuk.runly.ui.components.currentLocationMarker

private const val POLYLINE_WIDTH = 12f

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun MapContent(
    modifier: Modifier,
    isTracking: Boolean,
    isDarkTheme: Boolean,
    pathPoints: List<List<LatLng>>,
    currentLocation: LatLng?,
    cameraPositionState: CameraPositionState
) {
    val context = LocalContext.current
    var isSatellite by remember { mutableStateOf(false) }
    val mapProperties =
        MapProperties(mapType = if (isSatellite) MapType.SATELLITE else MapType.NORMAL)

    val mapStyleRes = if (isDarkTheme) {
        R.raw.map_night_style
    } else {
        R.raw.map_light_style
    }

    Box(modifier = modifier) {
        GoogleMap(
            modifier = Modifier.fillMaxWidth(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = MapUiSettings(
                zoomControlsEnabled = true,
                zoomGesturesEnabled = true,
                mapToolbarEnabled = true
            )
        ) {
            MapEffect(isDarkTheme) { map ->
                val mapStyle = MapStyleOptions.loadRawResourceStyle(context, mapStyleRes)
                map.setMapStyle(mapStyle)
            }
            if (!isTracking && currentLocation != null) {
                Marker(
                    state = MarkerState(currentLocation),
                    icon = currentLocationMarker,
                    anchor = Offset(0.5f, 0.5f)
                )
            }
            pathPoints.forEach { segment ->
                if (segment.isNotEmpty()) {
                    Polyline(
                        points = segment,
                        color = MaterialTheme.colorScheme.primary,
                        width = POLYLINE_WIDTH
                    )
                }
            }
            if (isTracking) {
                val lastSegment = pathPoints.lastOrNull()
                lastSegment?.lastOrNull()?.let { latestLocation ->
                    Marker(
                        state = MarkerState(latestLocation),
                        icon = currentLocationMarker,
                        anchor = Offset(0.5f, 0.5f)
                    )
                }
            }
        }
        IconButton(
            onClick = {
                isSatellite = !isSatellite
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = null
            )
        }
    }
}