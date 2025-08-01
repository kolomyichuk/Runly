package kolomyichuk.runly.data.remote.firestore.model

import kolomyichuk.runly.data.local.room.entity.LatLngPoint

data class RouteSegment(
    val points: List<LatLngPoint> = emptyList()
)
