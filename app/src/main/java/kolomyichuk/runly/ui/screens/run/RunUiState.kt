package kolomyichuk.runly.ui.screens.run

data class RunUiState(
    val hasForegroundLocationPermission: Boolean = false,
    val isTracking: Boolean = false,
    val isPause: Boolean = false,
    val distance: String = "",
)
