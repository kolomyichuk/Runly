package kolomyichuk.runly.ui.screens.rundetails

import androidx.annotation.RawRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import kolomyichuk.runly.LocalAppTheme
import kolomyichuk.runly.R
import kolomyichuk.runly.domain.run.model.RoutePoint
import kolomyichuk.runly.domain.settings.model.AppTheme
import kolomyichuk.runly.ui.components.finishMarker
import kolomyichuk.runly.ui.components.startMarker
import kolomyichuk.runly.ui.ext.toLatLng
import kolomyichuk.runly.ui.screens.run.POLYLINE_WIDTH

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun RunMapOverlays(
    pathPoints: List<List<RoutePoint>>
) {
    val context = LocalContext.current
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

    MapEffect(isDarkTheme) { map ->
        val mapStyle = MapStyleOptions.loadRawResourceStyle(context, mapStyleRes)
        map.setMapStyle(mapStyle)
    }

    pathPoints.forEach { segment ->
        Polyline(
            points = segment.map { it.toLatLng() },
            color = MaterialTheme.colorScheme.primary,
            width = POLYLINE_WIDTH
        )
    }

    val firstSegment = pathPoints.firstOrNull()
    firstSegment?.firstOrNull()?.let { firstLocation ->
        Marker(
            state = MarkerState(firstLocation.toLatLng()),
            icon = startMarker,
            anchor = Offset(x = 0.5f, y = 0.5f)
        )
    }

    val lastSegment = pathPoints.lastOrNull()
    lastSegment?.lastOrNull()?.let { lastLocation ->
        Marker(
            state = MarkerState(lastLocation.toLatLng()),
            icon = finishMarker,
            anchor = Offset(x = 0.5f, y = 0.5f)
        )
    }
}
