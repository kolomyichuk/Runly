package kolomyichuk.runly.domain.run.model

data class RunState(
    val isTracking: Boolean = false,
    val isActiveRun: Boolean = false,
    val isPause: Boolean = false,
    val timeInMillis: Long = 0,
    val distanceInMeters: Double = 0.00,
    val pathPoints: List<List<RoutePoint>> = emptyList()
)
