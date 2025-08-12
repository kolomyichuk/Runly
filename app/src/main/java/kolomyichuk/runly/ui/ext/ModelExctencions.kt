package kolomyichuk.runly.ui.ext

import com.google.android.gms.maps.model.LatLng
import kolomyichuk.runly.domain.run.model.RoutePoint

fun RoutePoint.toLatLng(): LatLng = LatLng(this.latitude, this.longitude)

fun LatLng.toRoutePoint(): RoutePoint = RoutePoint(this.latitude, this.longitude)