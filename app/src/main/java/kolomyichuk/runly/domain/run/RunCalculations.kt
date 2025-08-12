package kolomyichuk.runly.domain.run

import kolomyichuk.runly.domain.settings.model.DistanceUnit
import java.util.Locale

object RunCalculations {
    fun convertDistance(distanceImMeters: Double, unit: DistanceUnit): Double {
        return distanceImMeters / unit.metersPerUnit
    }

    fun calculateAvgSpeed(distance: Double, durationInMillis: Long): String {
        val timeInSeconds = durationInMillis / 1000
        return if (timeInSeconds > 5 && distance > 0.01) {
            val speed = distance / (timeInSeconds / 3600.0)
            if (speed.isFinite()) {
                String.format(Locale.US, "%.2f", speed)
            } else "0.00"
        } else "0.00"
    }
}