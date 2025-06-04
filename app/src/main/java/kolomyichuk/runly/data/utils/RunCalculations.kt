package kolomyichuk.runly.data.utils

import kolomyichuk.runly.data.model.DistanceUnit
import java.util.Locale

fun calculateAvgSpeed(distance: Double, durationInMillis: Long): String {
    val timeInSeconds = durationInMillis / 1000
    return if (timeInSeconds > 5 && distance > 0.01) {
        val speed = distance / (timeInSeconds / 3600.0)
        if (speed.isFinite()) String.format(Locale.US, "%.2f", speed) else "0.00"
    } else "0.00"
}

fun convertDistance(distanceInMeters: Double, unit: DistanceUnit): Double {
    return distanceInMeters / unit.metersPerUnit
}