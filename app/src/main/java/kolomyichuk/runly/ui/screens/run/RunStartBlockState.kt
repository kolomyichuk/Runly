package kolomyichuk.runly.ui.screens.run

data class RunStartBlockState(
    val isTracking: Boolean = false,
    val isPause: Boolean = false,
    val distance: String = ""
)
