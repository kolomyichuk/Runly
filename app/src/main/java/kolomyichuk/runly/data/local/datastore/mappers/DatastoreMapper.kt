package kolomyichuk.runly.data.local.datastore.mappers

import kolomyichuk.runly.data.local.datastore.model.DistanceUnitData
import kolomyichuk.runly.domain.settings.model.DistanceUnit

fun DistanceUnitData.toDomain(): DistanceUnit {
    return when(this){
        DistanceUnitData.MILES -> DistanceUnit.MILES
        DistanceUnitData.KILOMETERS -> DistanceUnit.KILOMETERS
    }
}

fun DistanceUnit.toDistanceUnitData():DistanceUnitData{
    return when(this){
        DistanceUnit.MILES -> DistanceUnitData.MILES
        DistanceUnit.KILOMETERS -> DistanceUnitData.KILOMETERS
    }
}