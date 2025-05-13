package kolomyichuk.runly.ui.screens.run

import android.Manifest
import android.content.pm.PackageManager
import android.os.Looper
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
import androidx.core.app.ActivityCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
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
import kolomyichuk.runly.R
import kolomyichuk.runly.ui.components.currentLocationMarker

const val POLYLINE_WIDTH = 12f
const val MAP_ZOOM = 15f

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun RunMapView(
    runViewModel: RunViewModel,
    isDarkTheme: Boolean,
    modifier: Modifier
) {
    val runState = runViewModel.runState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    var locationCallback by remember { mutableStateOf<LocationCallback?>(null) }

    LaunchedEffect(!runState.value.isTracking) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val request = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                5000L
            ).build()

            val callback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    result.lastLocation?.let {
                        currentLocation = LatLng(it.latitude, it.longitude)
                    }
                }
            }
            fusedLocationClient.requestLocationUpdates(request, callback, Looper.getMainLooper())
            locationCallback = callback
        } else {
            locationCallback?.let { fusedLocationClient.removeLocationUpdates(it) }
            locationCallback = null
        }
    }

    var isSatellite by remember { mutableStateOf(false) }
    val mapProperties =
        MapProperties(mapType = if (isSatellite) MapType.SATELLITE else MapType.NORMAL)

    val mapStyleRes = if (isDarkTheme) {
        R.raw.map_night_style
    } else {
        R.raw.map_light_style
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            if (runState.value.pathPoints.isNotEmpty() && runState.value.pathPoints.first().isNotEmpty()
            ) runState.value.pathPoints.first().first()
            else LatLng(49.010708, 25.796191), MAP_ZOOM
        )
    }

    LaunchedEffect(runState.value.isTracking, currentLocation, runState.value.pathPoints) {
        if (currentLocation != null) {
            if (runState.value.isTracking) {
                runState.value.pathPoints.lastOrNull()?.lastOrNull()?.let { latestLocation ->
                    cameraPositionState.animate(
                        CameraUpdateFactory.newLatLngZoom(latestLocation, MAP_ZOOM),
                        1000
                    )
                }
            } else {
                currentLocation?.let {
                    cameraPositionState.animate(
                        CameraUpdateFactory.newLatLngZoom(
                            currentLocation!!,
                            MAP_ZOOM
                        ), 1000
                    )
                }
            }
        }
    }

    Box(
        modifier = modifier
    ) {
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth(),
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
            if (!runState.value.isTracking && currentLocation != null) {
                Marker(
                    state = MarkerState(currentLocation!!),
                    icon = currentLocationMarker,
                    anchor = Offset(0.5f, 0.5f)
                )
            }
            runState.value.pathPoints.forEach { segment ->
                if (segment.isNotEmpty()) {
                    Polyline(
                        points = segment,
                        color = MaterialTheme.colorScheme.primary,
                        width = POLYLINE_WIDTH
                    )
                }
            }
            if (runState.value.isTracking) {
                val lastSegment = runState.value.pathPoints.lastOrNull()
                if (!lastSegment.isNullOrEmpty()) {
                    Marker(
                        state = MarkerState(lastSegment.last()),
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