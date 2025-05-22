package kolomyichuk.runly.ui.screens.run

import android.Manifest
import android.content.pm.PackageManager
import android.os.Looper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng

private const val LOCATION_UPDATE_INTERVAL = 5000L

@Composable
fun HandlePassiveLocationUpdates(
    isTracking: Boolean,
    fusedLocationClient: FusedLocationProviderClient,
    onLocationUpdate: (LatLng) -> Unit,
    onCallbackChanged: (LocationCallback?) -> Unit,
    locationCallback: LocationCallback?
) {
    val context = LocalContext.current
    DisposableEffect(key1 = !isTracking) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
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
            fusedLocationClient.requestLocationUpdates(request, callback, Looper.getMainLooper())
            onCallbackChanged(callback)
        }
        onDispose {
            locationCallback?.let { fusedLocationClient.removeLocationUpdates(it) }
            onCallbackChanged(null)
        }
    }
}