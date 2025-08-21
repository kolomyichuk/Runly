package kolomyichuk.runly.ui.screens.rundetails

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import kolomyichuk.runly.LocalAppTheme
import kolomyichuk.runly.R
import kolomyichuk.runly.domain.run.model.RoutePoint
import kolomyichuk.runly.domain.settings.model.AppTheme
import kolomyichuk.runly.ui.components.MapTypeToggleButton
import kolomyichuk.runly.ui.ext.toLatLng
import kolomyichuk.runly.ui.screens.run.MAP_ZOOM

@Composable
fun RunDetailsMapWithRoute(
    pathPoints: List<List<RoutePoint>>,
    modifier: Modifier = Modifier,
) {
    val cameraPositionState = rememberCameraPositionState()
    val firstLocation = pathPoints.firstOrNull()?.firstOrNull()?.toLatLng()

    LaunchedEffect(firstLocation) {
        if (firstLocation != null) {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(firstLocation, MAP_ZOOM)
            )
        }
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
            RunMapOverlays(
                pathPoints = pathPoints
            )
        }

        MapTypeToggleButton(
            modifier = Modifier.align(Alignment.TopEnd),
            onToggle = { isSatellite = !isSatellite }
        )
    }
}
