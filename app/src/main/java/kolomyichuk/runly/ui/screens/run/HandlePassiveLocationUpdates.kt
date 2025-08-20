package kolomyichuk.runly.ui.screens.run

import android.annotation.SuppressLint
import android.os.Looper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng

private const val LOCATION_UPDATE_INTERVAL = 5000L

@Suppress("LongParameterList")
@SuppressLint("MissingPermission")
@Composable
fun HandlePassiveLocationUpdates(
    isTracking: Boolean,
    fusedLocationClient: FusedLocationProviderClient,
    onLocationUpdate: (LatLng) -> Unit,
    onCallbackChanged: (LocationCallback?) -> Unit,
    locationCallback: LocationCallback?,
    hasForegroundLocationPermission: Boolean
) {
    DisposableEffect(key1 = !isTracking, key2 = hasForegroundLocationPermission) {
        if (hasForegroundLocationPermission) {
            val request = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                LOCATION_UPDATE_INTERVAL
            ).build()

            val callback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    result.lastLocation?.let {
                        onLocationUpdate(LatLng(it.latitude, it.longitude))
                    }
                }
            }
            fusedLocationClient.requestLocationUpdates(
                request,
                callback,
                Looper.getMainLooper()
            )
            onCallbackChanged(callback)
        }
        onDispose {
            locationCallback?.let { fusedLocationClient.removeLocationUpdates(it) }
            onCallbackChanged(null)
        }
    }
}
