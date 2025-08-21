package kolomyichuk.runly.utils

import kolomyichuk.runly.domain.run.model.RoutePoint
import kolomyichuk.runly.domain.run.model.Run

fun createRun1() = Run(
    id = "test-run-1",
    timestamp = 2555455,
    durationInMillis = 5000,
    distanceInMeters = 500.0,
    routePoints = listOf(listOf(RoutePoint(14.00, 48.44)))
)

fun createRun2() = Run(
    id = "test-run-2",
    timestamp = 2555455,
    durationInMillis = 9000,
    distanceInMeters = 500.0,
    routePoints = listOf(listOf(RoutePoint(12.00, 54.74)))
)

fun createRun3() = Run(
    id = "test-run-3",
    timestamp = 2555455,
    durationInMillis = 8000,
    distanceInMeters = 500.0,
    routePoints = listOf(listOf(RoutePoint(45.00, 28.44)))
)

fun createRunsList() = listOf(createRun1(), createRun2(), createRun3())