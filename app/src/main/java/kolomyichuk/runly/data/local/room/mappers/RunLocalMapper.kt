package kolomyichuk.runly.data.local.room.mappers

import kolomyichuk.runly.data.local.room.entity.RunEntity
import kolomyichuk.runly.data.model.Run

fun Run.toRunEntity(): RunEntity {
    return RunEntity(
        id = id.toInt(),
        timestamp = timestamp,
        durationInMillis = durationInMillis,
        distanceInMeters = distanceInMeters,
        routePoints = routePoints
    )
}