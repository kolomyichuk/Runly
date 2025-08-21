package kolomyichuk.runly.ui.screens.run

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import kolomyichuk.runly.LocalAppTheme
import kolomyichuk.runly.R
import kolomyichuk.runly.domain.run.model.RoutePoint
import kolomyichuk.runly.domain.settings.model.AppTheme

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun MapDisplay(
    isTracking: Boolean,
    pathPoints: List<List<RoutePoint>>,
    currentLocation: LatLng?,
    cameraPositionState: CameraPositionState,
    isSatellite: Boolean
) {
    val context = LocalContext.current
    val appTheme = LocalAppTheme.current
    val isDarkTheme = when (appTheme) {
        AppTheme.DARK -> true
        AppTheme.LIGHT -> false
        AppTheme.SYSTEM -> isSystemInDarkTheme()
    }
    val mapStyleRes = if (isDarkTheme) R.raw.map_night_style else R.raw.map_light_style

    val mapProperties = remember(isSatellite) {
        MapProperties(mapType = if (isSatellite) MapType.SATELLITE else MapType.NORMAL)
    }

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
        MapMarkers(isTracking, pathPoints, currentLocation)
    }
}
