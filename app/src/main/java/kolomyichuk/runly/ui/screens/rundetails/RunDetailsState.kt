package kolomyichuk.runly.ui.screens.rundetails

import com.google.android.gms.maps.model.LatLng

data class RunDetailsState(
    val dateTime: String = "",
    val duration: String = "",
    val distance: String = "",
    val avgSpeed: String = "",
    val pathPoints: List<List<LatLng>> = emptyList()
)