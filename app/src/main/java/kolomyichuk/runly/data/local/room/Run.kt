package kolomyichuk.runly.data.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "runs")
data class Run(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val timestamp: Long,
    val durationInMillis: Long,
    val distanceInMeters: Double,
    val avgSpeed: Float,
    val routePoints: List<List<LatLngPoint>>
)
