package kolomyichuk.runly.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "runs")
data class Run(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo val timestamp: Long,
    @ColumnInfo val durationInMillis: Long,
    @ColumnInfo val distanceInMeters: Double,
    @ColumnInfo val avgSpeed: Float,
    @ColumnInfo val routePoints: List<List<LatLngPoint>>
)
