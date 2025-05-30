package kolomyichuk.runly.data.local.room.entity

import kotlinx.serialization.Serializable

@Serializable
data class LatLngPoint(
    val latitude: Double,
    val longitude: Double
)
