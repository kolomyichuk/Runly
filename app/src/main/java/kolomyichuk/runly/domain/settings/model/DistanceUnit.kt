package kolomyichuk.runly.domain.settings.model

private const val METERS_IN_KILOMETER = 1000.0
private const val METERS_IN_MILE = 1609.344

enum class DistanceUnit(val metersPerUnit: Double) {
    KILOMETERS(METERS_IN_KILOMETER),
    MILES(METERS_IN_MILE)
}
