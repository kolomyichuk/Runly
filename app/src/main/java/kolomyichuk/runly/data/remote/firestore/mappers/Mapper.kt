package kolomyichuk.runly.data.remote.firestore.mappers

import kolomyichuk.runly.data.model.Run
import kolomyichuk.runly.data.remote.firestore.model.RouteSegment
import kolomyichuk.runly.data.remote.firestore.model.RunFirestoreModel

fun RunFirestoreModel.toRun(): Run {
    return Run(
        id = id,
        timestamp = timestamp,
        durationInMillis = durationInMillis,
        distanceInMeters = distanceInMeters,
        routePoints = routePoints.map { it.points }
    )
}

fun Run.toRunFirestoreModel(userId: String): RunFirestoreModel {
    return RunFirestoreModel(
        id = id,
        userId = userId,
        timestamp = timestamp,
        durationInMillis = durationInMillis,
        distanceInMeters = distanceInMeters,
        routePoints = routePoints.map { segment ->
            RouteSegment(points = segment)
        }
    )
}