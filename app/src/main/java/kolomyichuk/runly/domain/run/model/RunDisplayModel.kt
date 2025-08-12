package kolomyichuk.runly.domain.run.model

import kolomyichuk.runly.domain.settings.model.DistanceUnit

data class RunDisplayModel(
    val id: String = "",
    val dateTime: String = "",
    val distance: String = "0,0",
    val duration: String = "00:00",
    val avgSpeed: String = "0,00",
    val distanceInMeters: Double = 0.00,
    val timeInMillis: Long = 0,
    val routePoints: List<List<RoutePoint>> = emptyList(),
    val unit: DistanceUnit = DistanceUnit.KILOMETERS,
    val isTracking: Boolean = false,
    val isPause: Boolean = false,
    val isActiveRun: Boolean = false
)
