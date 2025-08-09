package kolomyichuk.runly.data.local.room.mappers

import kolomyichuk.runly.data.local.room.entity.LatLngPoint
import kolomyichuk.runly.data.local.room.entity.RunEntity
import kolomyichuk.runly.domain.run.model.RoutePoint
import kolomyichuk.runly.domain.run.model.Run

fun Run.toRunEntity(): RunEntity {
    return RunEntity(
        id = id.toInt(),
        timestamp = timestamp,
        durationInMillis = durationInMillis,
        distanceInMeters = distanceInMeters,
        routePoints = routePoints.map { path ->
            path.map {
                LatLngPoint(it.latitude, it.longitude)
            }
        }
    )
}

fun RunEntity.toRun(): Run {
    return Run(
        id = id.toString(),
        timestamp = timestamp,
        durationInMillis = durationInMillis,
        distanceInMeters = distanceInMeters,
        routePoints = routePoints.map { path ->
            path.map {
                RoutePoint(it.latitude, it.longitude)
            }
        }
    )
}