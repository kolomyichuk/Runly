package kolomyichuk.runly.ui.screens.profile

data class UserProfile(
    val name: String? = null,
    val photoUrl: String? = null,
    val thisWeekDistanceByDay: List<Float> = List(size = 7) { 0f },
    val totalDistance: Float = 0f
)
