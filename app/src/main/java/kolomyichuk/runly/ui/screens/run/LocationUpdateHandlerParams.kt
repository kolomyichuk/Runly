package kolomyichuk.runly.ui.screens.run

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.maps.model.LatLng

data class LocationUpdateHandlerParams(
    val isTracking: Boolean,
    val fusedLocationClient: FusedLocationProviderClient,
    val onLocationUpdate: (LatLng) -> Unit,
    val onCallbackChanged: (LocationCallback?) -> Unit,
    val locationCallback: LocationCallback?,
    val hasForegroundLocationPermission: Boolean
)
