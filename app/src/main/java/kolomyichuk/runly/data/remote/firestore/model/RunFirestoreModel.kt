package kolomyichuk.runly.data.remote.firestore.model

data class RunFirestoreModel(
    val id: String = "",
    val userId: String = "",
    val timestamp: Long = 0L,
    val durationInMillis: Long = 0L,
    val distanceInMeters: Double = 0.0,
    val routePoints: List<RoutePointFirestore> = emptyList()
)