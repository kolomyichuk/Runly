package kolomyichuk.runly.data.local.room.mappers

import kolomyichuk.runly.data.local.room.entity.RunEntity
import kolomyichuk.runly.data.model.Run

fun RunEntity.fromRunEntityToRun(): Run {
    return Run(
        id = id.toString(),
        timestamp = timestamp,
        durationInMillis = durationInMillis,
        distanceInMeters = distanceInMeters,
        routePoints = routePoints
    )
}

fun Run.fromRunToRunEntity():RunEntity{
    return RunEntity(
        id = id.toInt(),
        timestamp = timestamp,
        durationInMillis = durationInMillis,
        distanceInMeters = distanceInMeters,
        routePoints = routePoints
    )
}