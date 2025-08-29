package kolomyichuk.runly.service.manager

import android.annotation.SuppressLint
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import kolomyichuk.runly.domain.run.repository.RemoteRunRepository
import kolomyichuk.runly.ui.ext.toLatLng
import kolomyichuk.runly.ui.ext.toRoutePoint

private const val LOCATION_UPDATE_INTERVAL = 3000L

class RunLocationManager(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val remoteRunRepository: RemoteRunRepository
) {
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            locationResult.locations.lastOrNull()?.let { location ->
                val newPoint = LatLng(location.latitude, location.longitude)
                addLocationPoints(newPoint)
                val currentPath = remoteRunRepository.runState.value.pathPoints
                val segmentDistance = currentPath.map { segment ->
                    SphericalUtil.computeLength(segment.map { it.toLatLng() })
                }
                val totalDistance = segmentDistance.sum()
                remoteRunRepository.updateRunState { copy(distanceInMeters = totalDistance) }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun startLocationTracking() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            LOCATION_UPDATE_INTERVAL
        )
            .setMinUpdateDistanceMeters(1f)
            .build()

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun stopLocationTracking() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun addLocationPoints(point: LatLng) {
        val updatedPath = remoteRunRepository.runState.value.pathPoints.toMutableList()
        if (updatedPath.isEmpty()) {
            updatedPath.add(listOf(point.toRoutePoint()))
        } else {
            val lastSegment = updatedPath.last().toMutableList()
            lastSegment.add(point.toRoutePoint())
            updatedPath[updatedPath.lastIndex] = lastSegment
        }
        remoteRunRepository.updateRunState {
            copy(pathPoints = updatedPath.toList())
        }
    }
}
