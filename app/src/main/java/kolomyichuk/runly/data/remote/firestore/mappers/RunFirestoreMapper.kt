package kolomyichuk.runly.data.remote.firestore.mappers

import kolomyichuk.runly.data.remote.firestore.model.LatLngPointFirestore
import kolomyichuk.runly.domain.run.model.Run
import kolomyichuk.runly.data.remote.firestore.model.RoutePointFirestore
import kolomyichuk.runly.data.remote.firestore.model.RunFirestoreModel
import kolomyichuk.runly.domain.run.model.RoutePoint

fun RunFirestoreModel.toRun(): Run {
    return Run(
        id = id,
        timestamp = timestamp,
        durationInMillis = durationInMillis,
        distanceInMeters = distanceInMeters,
        routePoints = routePoints.map { path ->
            path.points.map { latLngPointFirestore ->
                RoutePoint(latLngPointFirestore.latitude, latLngPointFirestore.longitude)
            }
        }
    )
}

fun Run.toRunFirestoreModel(userId: String): RunFirestoreModel {
    return RunFirestoreModel(
        id = id,
        userId = userId,
        timestamp = timestamp,
        durationInMillis = durationInMillis,
        distanceInMeters = distanceInMeters,
        routePoints = routePoints.map { path ->
            RoutePointFirestore(
                points = path.map { routePoint ->
                    LatLngPointFirestore(routePoint.latitude, routePoint.longitude)
                }
            )
        }
    )
}
