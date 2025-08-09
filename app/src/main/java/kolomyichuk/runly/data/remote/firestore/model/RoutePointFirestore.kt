package kolomyichuk.runly.data.remote.firestore.model

data class RoutePointFirestore(
    val points: List<LatLngPointFirestore> = emptyList()
)
