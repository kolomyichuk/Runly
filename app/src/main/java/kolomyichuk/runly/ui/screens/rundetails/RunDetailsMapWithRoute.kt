package kolomyichuk.runly.ui.screens.rundetails

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kolomyichuk.runly.LocalAppTheme
import kolomyichuk.runly.R
import kolomyichuk.runly.data.local.datastore.AppTheme
import kolomyichuk.runly.ui.components.MapTypeToggleButton
import kolomyichuk.runly.ui.components.finishMarker
import kolomyichuk.runly.ui.components.startMarker
import kolomyichuk.runly.ui.screens.run.MAP_ZOOM
import kolomyichuk.runly.ui.screens.run.POLYLINE_WIDTH

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun RunDetailsMapWithRoute(
    pathPoints: List<List<LatLng>>,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val cameraPositionState = rememberCameraPositionState()
    val firstLocation = pathPoints.firstOrNull()?.firstOrNull()

    LaunchedEffect(firstLocation) {
        if (firstLocation != null) {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(firstLocation, MAP_ZOOM)
            )
        }
    }

    val appTheme = LocalAppTheme.current
    val isDarkTheme = when (appTheme) {
        AppTheme.DARK -> true
        AppTheme.LIGHT -> false
        AppTheme.SYSTEM -> isSystemInDarkTheme()
    }
    val mapStyleRes = if (isDarkTheme) {
        R.raw.map_night_style
    } else {
        R.raw.map_light_style
    }

    var isSatellite by remember { mutableStateOf(false) }
    val mapProperties = remember(isSatellite) {
        MapProperties(mapType = if (isSatellite) MapType.SATELLITE else MapType.NORMAL)
    }

    Box(modifier = modifier) {
        GoogleMap(
            modifier = Modifier.padding(vertical = 5.dp),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = MapUiSettings(
                zoomGesturesEnabled = true,
                zoomControlsEnabled = true,
                mapToolbarEnabled = true
            )
        ) {
            MapEffect(isDarkTheme) { map ->
                val mapStyle = MapStyleOptions.loadRawResourceStyle(context, mapStyleRes)
                map.setMapStyle(mapStyle)
            }
            pathPoints.forEach { segment ->
                Polyline(
                    points = segment,
                    color = MaterialTheme.colorScheme.primary,
                    width = POLYLINE_WIDTH
                )
            }

            val firstSegment = pathPoints.firstOrNull()
            firstSegment?.firstOrNull()?.let { firstLocation ->
                Marker(
                    state = MarkerState(firstLocation),
                    icon = startMarker,
                    anchor = Offset(0.5f, 0.5f)
                )
            }

            val lastSegment = pathPoints.lastOrNull()
            lastSegment?.lastOrNull()?.let { lastLocation ->
                Marker(
                    state = MarkerState(lastLocation),
                    icon = finishMarker,
                    anchor = Offset(0.5f, 0.5f)
                )
            }
        }
        MapTypeToggleButton(
            modifier = Modifier.align(Alignment.TopEnd),
            onToggle = { isSatellite = !isSatellite }
        )
    }
}