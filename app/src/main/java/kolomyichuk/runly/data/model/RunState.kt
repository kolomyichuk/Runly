package kolomyichuk.runly.data.model

import com.google.android.gms.maps.model.LatLng

data class RunState(
    val isTracking: Boolean = false,
    val isActiveRun: Boolean = false,
    val isPause: Boolean = false,
    val timeInMillis: Long = 0,
    val distanceInMeters: Double = 0.00,
    val avgSpeed: Float = 0.00f,
    val pathPoints: List<List<LatLng>> = emptyList()
)
