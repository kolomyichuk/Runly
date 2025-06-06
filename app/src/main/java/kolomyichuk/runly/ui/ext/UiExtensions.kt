package kolomyichuk.runly.ui.ext

import kolomyichuk.runly.R
import kolomyichuk.runly.data.model.DistanceUnit

fun DistanceUnit.getUnitLabel(): Int {
    return when (this) {
        DistanceUnit.KILOMETERS -> R.string.km
        DistanceUnit.MILES -> R.string.miles
    }
}