package kolomyichuk.runly.ui.ext

import com.google.android.gms.maps.model.LatLng
import kolomyichuk.runly.R
import kolomyichuk.runly.domain.run.model.RoutePoint
import kolomyichuk.runly.domain.settings.model.DistanceUnit

fun DistanceUnit.getUnitLabel(): Int {
    return when (this) {
        DistanceUnit.KILOMETERS -> R.string.km
        DistanceUnit.MILES -> R.string.miles
    }
}

fun RoutePoint.toLatLng(): LatLng = LatLng(this.latitude, this.longitude)

fun LatLng.toRoutePoint(): RoutePoint = RoutePoint(this.latitude, this.longitude)
