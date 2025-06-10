package kolomyichuk.runly.data.model

import com.google.android.gms.maps.model.LatLng

data class RunDisplayModel(
    val id: Int = 0,
    val dateTime: String = "",
    val distance: String = "0,0",
    val duration: String = "00:00",
    val avgSpeed: String = "0,00",
    val routePoints: List<List<LatLng>> = emptyList(),
    val unit: DistanceUnit = DistanceUnit.KILOMETERS,
    val isTracking: Boolean = false,
    val isPause: Boolean = false,
    val isActiveRun: Boolean = false
)
