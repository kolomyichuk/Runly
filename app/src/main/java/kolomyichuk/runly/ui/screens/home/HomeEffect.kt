package kolomyichuk.runly.ui.screens.home

sealed interface HomeEffect {
    data object ShowDeleteSnackBar : HomeEffect
}
