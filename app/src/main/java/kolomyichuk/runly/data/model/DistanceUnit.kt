package kolomyichuk.runly.data.model

enum class DistanceUnit(val metersPerUnit: Double) {
    KILOMETERS(1000.0),
    MILES(1609.344)
}