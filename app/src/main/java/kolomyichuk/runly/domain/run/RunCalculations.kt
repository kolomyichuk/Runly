package kolomyichuk.runly.domain.run

import kolomyichuk.runly.domain.settings.model.DistanceUnit
import java.util.Locale

object RunCalculations {

    private const val MILLIS_IN_SECOND = 1000
    private const val MIN_TIME_SECONDS = 5
    private const val MIN_DISTANCE_KM = 0.01
    private const val SECONDS_IN_HOUR = 3600.0

    fun convertDistance(distanceImMeters: Double, unit: DistanceUnit): Double {
        return distanceImMeters / unit.metersPerUnit
    }

    fun calculateAvgSpeed(distance: Double, durationInMillis: Long): String {
        val timeInSeconds = durationInMillis / MILLIS_IN_SECOND
        return if (timeInSeconds > MIN_TIME_SECONDS && distance > MIN_DISTANCE_KM) {
            val speed = distance / (timeInSeconds / SECONDS_IN_HOUR)
            if (speed.isFinite()) {
                String.format(Locale.US, "%.2f", speed)
            } else "0.00"
        } else "0.00"
    }
}
