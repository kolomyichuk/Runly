package kolomyichuk.runly.data.local.room.entity

import kotlinx.serialization.Serializable

@Serializable
data class LatLngPoint(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)
