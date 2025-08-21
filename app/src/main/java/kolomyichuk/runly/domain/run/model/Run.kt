package kolomyichuk.runly.domain.run.model

data class Run(
    val id: String = "",
    val timestamp: Long = 0L,
    val durationInMillis: Long = 0L,
    val distanceInMeters: Double = 0.0,
    val routePoints: List<List<RoutePoint>> = emptyList()
)
