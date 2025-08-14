package kolomyichuk.runly.domain.run.ext

import kolomyichuk.runly.domain.run.RunCalculations
import kolomyichuk.runly.domain.run.model.RoutePoint
import kolomyichuk.runly.domain.run.model.Run
import kolomyichuk.runly.domain.run.model.RunDisplayModel
import kolomyichuk.runly.domain.settings.model.DistanceUnit
import kolomyichuk.runly.utils.FormatterUtils
import kolomyichuk.runly.utils.FormatterUtils.toFormattedDateTime
import java.util.Locale

fun Run.toRunDisplayModel(unit: DistanceUnit): RunDisplayModel {
    val distance = RunCalculations.convertDistance(distanceInMeters, unit)
    val avgSpeed = RunCalculations.calculateAvgSpeed(distance, durationInMillis)

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