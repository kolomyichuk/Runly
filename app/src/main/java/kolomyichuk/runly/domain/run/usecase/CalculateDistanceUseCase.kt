package kolomyichuk.runly.domain.run.usecase

import kolomyichuk.runly.domain.settings.model.DistanceUnit
import javax.inject.Inject

class CalculateDistanceUseCase @Inject constructor() {
    operator fun invoke(distanceInMeters: Double, unit: DistanceUnit): Double {
        return distanceInMeters / unit.metersPerUnit
    }
}