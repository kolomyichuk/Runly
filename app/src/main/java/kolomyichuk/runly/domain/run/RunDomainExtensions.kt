package kolomyichuk.runly.domain.run

import kolomyichuk.runly.domain.run.model.RoutePoint
import kolomyichuk.runly.domain.run.model.Run
import kolomyichuk.runly.domain.run.model.RunDisplayModel
import kolomyichuk.runly.domain.settings.model.DistanceUnit
import kolomyichuk.runly.utils.FormatterUtils
import kolomyichuk.runly.utils.FormatterUtils.toFormattedDateTime
import java.util.Locale

fun Double.toDistanceUnit(unit: DistanceUnit): Double {
    return this / unit.metersPerUnit
}

fun Double.avgSpeed(durationInMillis: Long): String {
    val timeInSeconds = durationInMillis / 1000
    return if (timeInSeconds > 5 && this > 0.01) {
        val speed = this / (timeInSeconds / 3600.0)
        if (speed.isFinite()) {
            String.format(Locale.US, "%.2f", speed)
        } else "0.00"
    } else "0.00"
}

fun Run.toRunDisplayModel(unit: DistanceUnit): RunDisplayModel {
    val distance = this.distanceInMeters.toDistanceUnit(unit)
    val avgSpeed = distance.avgSpeed(this.durationInMillis)

    return RunDisplayModel(
        id = id,
        distance = String.format(Locale.US, "%.2f", distance),
        duration = FormatterUtils.formatTime(durationInMillis),
        routePoints = routePoints.map { path ->
            path.map {
                RoutePoint(it.latitude, it.longitude)
            }
        },
        avgSpeed = avgSpeed,
        dateTime = timestamp.toFormattedDateTime(),
        unit = unit
    )
}